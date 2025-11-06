package com.datasoft.luncheon.weekend;


import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeekendService {
    private final WeekendRepository weekendRepository;
    private final UserService userService;
    private final JdbcFunctionDao jdbcFunctionDao;

    public ResponseEntity<ApiResponse> insertHoliday(List<Weekend> weekend) {
        String userId =  userService.getCurrentUserId();
        Integer user = getUserId(userId);
        weekend.forEach(week -> {
            week.setOrgId(user);
            week.setCreatedBy(user); // Set created_by
            week.setUpdatedBy(user); // Set updated_by
        });

        weekendRepository.deleteAll();
        weekendRepository.saveAll(weekend);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<Weekend>> showHoliday() {
        return new ResponseEntity<>(weekendRepository.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteHoliday(Integer id){
        String currentUser = userService.getCurrentUserId();
        Integer currentUserId = user(currentUser);
        String insertSql =
                "INSERT INTO his_conf_holiday (conf_holiday_id, event_name, event_date, event_description, org_id, created_by, created_at, " +
                        "updated_by, updated_at, action_type, action_by) " +
                        "SELECT id,event_name, event_date, event_description, org_id, created_by, created_at, " +
                        "updated_by, updated_at, 'DELETED' as action_type, ? as action_by " +
                        "FROM conf_holiday " +
                        "WHERE id = ?";

        jdbcFunctionDao.getJdbcTemplate().update(insertSql, currentUserId, id);
        String deleteSql = "DELETE FROM conf_holiday WHERE id = ?";
        jdbcFunctionDao.getJdbcTemplate().update(deleteSql, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Integer getUserId(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

    public Integer user(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

}
