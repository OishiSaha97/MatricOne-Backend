package com.datasoft.luncheon.user;


import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${AUTH_API}")
    String apiUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtTokenProvider tokenProvider;
    private final JdbcFunctionDao jdbcFunctionDao;
    private final PasswordEncoder passwordEncoder;


    public String getCurrentUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName().toLowerCase();
    }


//    public String getAllUser(){
//        jdbcFunctionDao.getJdbcTemplate().update("SELECT username FROM users", username.trim().toLowerCase());
//        return apiResponse;
//    }



    public Map<String, Object> authenticate(String username, String password) throws UserPrincipalNotFoundException {
        if(isUserLuncheonLocal(username)){
           return authenticateLocally(username, password);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("context", "DSLive_Entities");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("UserName", username);
        body.add("PlainPassword", password);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            Map<String, Object> apiResponse = new HashMap<>();
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                apiResponse = mapper.readValue(responseEntity.getBody(), Map.class);
            }
            if(Objects.equals(apiResponse.get("isLoginSuccess"), true)){
                apiResponse.put("token", tokenProvider.generateToken(username));
                jdbcFunctionDao.getJdbcTemplate().update("UPDATE users set full_name = ? WHERE LOWER(username) = ?", apiResponse.get("Name"), username.trim().toLowerCase());
                return apiResponse;
            } else {
                throw new UsernameNotFoundException("Invalid Credentials");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new UserPrincipalNotFoundException("User not found");
    }

    private Map<String, Object>  authenticateLocally(String username, String password) {
        try{
            Map<String, Object> user = jdbcFunctionDao.getJdbcTemplate().queryForMap("SELECT full_name, password FROM users WHERE LOWER(username) = LOWER(?)", username);
            if(passwordEncoder.matches(password, user.get("password").toString())){
                Map<String, Object> response = new HashMap<>();
                response.put("Name", user.get("full_name"));
                response.put("token", tokenProvider.generateToken(username));
                return response;
            }
        } catch (Exception e){
            e.printStackTrace();

        }
        throw new UsernameNotFoundException("Invalid Credentials");
    }

    private boolean isUserLuncheonLocal(String username) {
        List list = jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT id FROM users where lower(username) = lower(?) AND is_luncheon_user = true", username);
        return !list.isEmpty();

    }

    public boolean existsByUsername(String username) {
        return !jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT id FROM users WHERE LOWER(username) = ?", username.toLowerCase().trim()).isEmpty();
    }

    public ResponseEntity<?> checkPermission() {
        String currentId = getCurrentUserId();
        if(!Objects.equals(currentId, "anonymoususer")){
            Map<String, Object> param = new LinkedHashMap<>();
            param.put("pUsername", currentId);
            if(Objects.equals(jdbcFunctionDao.getFunctionResult("fn_check_permission", param), "admin")){
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }


    public boolean isAdmin(){
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("pUsername", getCurrentUserId());
        return  Objects.equals(jdbcFunctionDao.getFunctionResult("fn_check_permission", param), "admin");
    }

    public ResponseEntity<?> userIds() {
        return new ResponseEntity<>(jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT UPPER(username) as name, full_name as fullName  FROM user_details"), HttpStatus.OK);
    }

    public ResponseEntity<?> userList(Long page, String team, String employeeType, String searchParam) {
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("page", page);
        param.put("team", team);
        param.put("employeeType", employeeType);
        param.put("searchParam", searchParam);
        return new ResponseEntity<>(jdbcFunctionDao.getProcedureResult("sp_get_users", param).get("#result-set-1"), HttpStatus.OK);


    }

    public ResponseEntity<?> createUser(UserDto user) {
        if(isAdmin()){
            if(!jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT id FROM user_details WHERE LOWER(username) = LOWER(?)", user.getUsername()).isEmpty()){
                // If another user exists with same Id
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            final String QUERY_USER_DETAILS = "INSERT INTO user_details (username, employee_id, full_name, team_name, employee_type) VALUES (?, ?, ?, ?, ?)";
            jdbcFunctionDao.getJdbcTemplate().update(QUERY_USER_DETAILS, user.getUsername(), user.getUsername().toUpperCase(), user.getFullName(), user.getTeam(), user.getEmployeeType());
            final String QUERY_USER = "INSERT INTO users (username, full_name, is_luncheon_user, password) VALUES (?, ?, true, ?)";
            jdbcFunctionDao.getJdbcTemplate().update(QUERY_USER, user.getUsername(),  user.getFullName(), this.encrypt(user.getPassword()));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private String encrypt(String password) {
        return passwordEncoder.encode(password);
    }


    public static boolean isNotLoggedIn(String userId){
        return userId == null || userId.isEmpty() || userId.equalsIgnoreCase("anonymoususer");
    }

}
