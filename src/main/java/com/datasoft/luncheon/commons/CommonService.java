package com.datasoft.luncheon.commons;


import com.datasoft.luncheon.commons.model.PagedResponse;
import com.datasoft.luncheon.commons.model.filter.SearchFilter;
import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.utils.DatabaseUtils;
import com.datasoft.luncheon.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.datasoft.luncheon.commons.model.Strings.FORBIDDEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;
    private final ExcelProcessorService excelProcessorService;

    public ResponseEntity<PagedResponse> getList(String source, SearchFilter params){
        String table = DatabaseUtils.getTableName(source);
        return new ResponseEntity<>(jdbcFunctionDao.getList(table, params), HttpStatus.OK);
    }



    public ResponseEntity<?> uploadExcel(String source, MultipartFile file, Integer id){
       if(userService.isAdmin()){
           try{
               String table = DatabaseUtils.getTableName(source);
               List<Map<String, Object>> list = excelProcessorService.readExcel(file, DatabaseUtils.columnMappings(table));
               String userId = userService.getCurrentUserId();
               Integer user = user(userId);
               jdbcFunctionDao.bulkInsert(table, list, id, user);
               return new ResponseEntity<>("File uploaded", HttpStatus.OK);
           } catch (Exception e){
               log.error(e.getMessage(), e.getCause());
               return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
           }
       }
        return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
    }

    public void downloadTable(String source, SearchFilter params, HttpServletResponse response){
        String table = DatabaseUtils.getTableName(source);
        excelProcessorService.writeExcel(
                jdbcFunctionDao.getList(table, params).getContent(),
                DatabaseUtils.columnMappings(table),
                response
        );
    }

    public ResponseEntity<?> findById(String source, Integer id) {
        String table = DatabaseUtils.getTableName(source);
        return new ResponseEntity<>(jdbcFunctionDao.findById(table, id), HttpStatus.OK);
    }

    public ResponseEntity<?> update(String source,Map<String, Object> dataSource){
        try {
            String table = DatabaseUtils.getTableName(source);
            jdbcFunctionDao.update(table, DatabaseUtils.columnMappings(table), dataSource);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (Exception e){
            log.error("Error saving debit transactions: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Integer user(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

}
