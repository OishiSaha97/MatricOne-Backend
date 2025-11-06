package com.datasoft.luncheon.meal;


import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.datasoft.luncheon.commons.model.Strings.FORBIDDEN;
import static com.datasoft.luncheon.commons.utils.DatabaseUtils.SP_GET_CALENDER;
import static com.datasoft.luncheon.commons.utils.DatabaseUtils.SP_GET_LUNCH_LIST;
import static com.datasoft.luncheon.user.UserService.isNotLoggedIn;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {

    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;


    public Object findCurrentMeal() {
        final String query =
                "SELECT cl.meal_a\n" +
                ", cl.meal_b\n" +
                ", cs.breakfast\n" +
                ", cs.snacks\n" +
                ", l.lunch \n" +
                "FROM conf_lunch cl\n" +
                "LEFT JOIN conf_snacks cs ON cl.`date` = cs.date\n" +
                "LEFT JOIN lunch l ON cl.`date` = l.date AND LOWER(l.user_id) = ?\n" +
                "WHERE cl.`date` = ?";
        List<Map<String, Object>> data =jdbcFunctionDao.getJdbcTemplate().queryForList(query, userService.getCurrentUserId(), today());
        return data.isEmpty()? Collections.emptyMap() : data.get(0);
    }

    public Object findMealDate(Date date) {
        final String query =
                "SELECT cl.meal_a\n" +
                        ", cl.meal_b\n" +
                        ", cs.breakfast\n" +
                        ", cs.snacks\n" +
                        ", l.lunch \n" +
                        "FROM conf_lunch cl\n" +
                        "LEFT JOIN conf_snacks cs ON cl.`date` = cs.date\n" +
                        "LEFT JOIN lunch l ON cl.`date` = l.date AND LOWER(l.user_id )= ?\n" +
                        "WHERE cl.`date` = ?";
        List<Map<String, Object>> data =jdbcFunctionDao.getJdbcTemplate().queryForList(query, userService.getCurrentUserId(), getDate(date));
        return data.isEmpty()? Collections.emptyMap() : data.get(0);
    }

//    public ResponseEntity<?> bookLunch(List<Lunch> lunchList) {
//
//        List<Lunch> lnchList=lunchList.stream().filter()
//        List<Lunch> lnchDelList=lunchList.stream().filter()
//
//
//        String userId = userService.getCurrentUserId();
//        Integer user = user(userId);
//        if(isNotLoggedIn(userId)){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        Date allowedDate = getMaxAllowedDate();
//        List<Lunch> lunchToUpdate = new ArrayList<>();
//        lunchList.forEach(lunch -> {
////            if(Objects.nonNull(lunch.getDate()) && (Objects.equals(lunch.getDate(), allowedDate) || lunch.getDate().after(allowedDate))){
//                lunchToUpdate.add(lunch);
////            }
//        });
//        String sql = "INSERT INTO `lunch` (`date`, `lunch`, `user_id`, `created_by`) " +
//                "VALUES (?, ?, ?, ?) " +
//                "ON DUPLICATE KEY UPDATE lunch = VALUES(lunch), updated_by = ?, updated_at = NOW()";
//        jdbcFunctionDao.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
////                String dateString = lunchToUpdate.get(i).getDate();
////                java.sql.Date sqlDate = java.sql.Date.valueOf(dateString);
////                preparedStatement.setDate(1, sqlDate);
//               // preparedStatement.setDate(1, new java.sql.Date(lunchToUpdate.get(i).getDate().getTime()));
//                Long timestamp = Long.valueOf(lunchToUpdate.get(i).getDate()); // Assuming it's a long (milliseconds)
//
//                // Convert timestamp (milliseconds) to java.sql.Date
//                java.sql.Date sqlDate = new java.sql.Date(timestamp);
//                preparedStatement.setDate(1, sqlDate);
//                preparedStatement.setString(2, lunchToUpdate.get(i).getLunch());
//                preparedStatement.setString(3, userId);
//                preparedStatement.setInt(4, user);
//                preparedStatement.setInt(5, user);
//            }
//
//            @Override
//            public int getBatchSize() {
//                return lunchToUpdate.size();
//            }
//        });
//        return new ResponseEntity<>(HttpStatus.OK);
//
//    }


//    public ResponseEntity<?> bookLunch(List<Lunch> lunchList) {
//
//        // Filter lunchList where lunch is not null or empty
//        List<Lunch> lnchList = lunchList.stream()
//                .filter(lunch -> lunch.getLunch() != null && !lunch.getLunch().trim().isEmpty())
//                .collect(Collectors.toList());
//
//        // Filter lunchList where lunch is null or empty
//        List<Lunch> lnchDelList = lunchList.stream()
//                .filter(lunch -> lunch.getLunch() == null || lunch.getLunch().trim().isEmpty())
//                .collect(Collectors.toList());
//
//        String userId = userService.getCurrentUserId();
//        Integer user = user(userId);
//        if (isNotLoggedIn(userId)) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        // ✅ 1. Handle the non-null lunch list (INSERT/UPDATE)
//        if (!lnchList.isEmpty()) {
//            String insertSql = "INSERT INTO `lunch` (`date`, `lunch`, `user_id`, `created_by`) " +
//                    "VALUES (?, ?, ?, ?) " +
//                    "ON DUPLICATE KEY UPDATE lunch = VALUES(lunch), updated_by = ?, updated_at = NOW()";
//
//            jdbcFunctionDao.getJdbcTemplate().batchUpdate(insertSql, new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
//                    Long timestamp = Long.valueOf(lnchList.get(i).getDate()); // Assuming timestamp is in milliseconds
//                    java.sql.Date sqlDate = new java.sql.Date(timestamp);
//                    preparedStatement.setDate(1, sqlDate);
//                    preparedStatement.setString(2, lnchList.get(i).getLunch());
//                    preparedStatement.setString(3, userId);
//                    preparedStatement.setInt(4, user);
//                    preparedStatement.setInt(5, user);
//                }
//
//                @Override
//                public int getBatchSize() {
//                    return lnchList.size();
//                }
//            });
//        }
//
//        // ✅ 2. Handle the null lunch list (UPDATE to NULL)
//        if (!lnchDelList.isEmpty()) {
//            String updateSql = "UPDATE `lunch` SET lunch = NULL, updated_by = ?, updated_at = NOW() WHERE `date` = ? AND user_id = ?";
//
//            jdbcFunctionDao.getJdbcTemplate().batchUpdate(updateSql, new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
//                    Long timestamp = Long.valueOf(lnchDelList.get(i).getDate()); // Assuming timestamp is in milliseconds
//                    java.sql.Date sqlDate = new java.sql.Date(timestamp);
//                    preparedStatement.setInt(1, user);
//                    preparedStatement.setDate(2, sqlDate);
//                    preparedStatement.setString(3, userId);
//                }
//
//                @Override
//                public int getBatchSize() {
//                    return lnchDelList.size();
//                }
//            });
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    public ResponseEntity<?> bookLunch(List<Lunch> updatedItems, List<Lunch> deletedItems, List<Lunch> newlyAddedItems) {

        String userId = userService.getCurrentUserId();
        Integer user = user(userId);
        if (isNotLoggedIn(userId)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Handle Newly Added Items (INSERT)
        if (!newlyAddedItems.isEmpty()) {
            String insertSql = "INSERT INTO `lunch` (`date`, `lunch`, `user_id`, `created_by`, `created_at`) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "`lunch` = VALUES(`lunch`), " +
                    "`updated_by` = VALUES(`created_by`), " +
                    "`updated_at` = NOW()";

            jdbcFunctionDao.getJdbcTemplate().batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long timestamp = Long.valueOf(newlyAddedItems.get(i).getDate()); // Assuming timestamp is in milliseconds
                    java.sql.Date sqlDate = new java.sql.Date(timestamp);
                    preparedStatement.setDate(1, sqlDate);
                    preparedStatement.setString(2, newlyAddedItems.get(i).getLunch());
                    preparedStatement.setString(3, userId);
                    preparedStatement.setInt(4, user);
                    preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // created_at
                }

                @Override
                public int getBatchSize() {
                    return newlyAddedItems.size();
                }
            });
        }

        // Handle Updated Items (UPDATE)
        if (!updatedItems.isEmpty()) {
            String updateSql = "UPDATE `lunch` SET lunch = ?, updated_by = ?, updated_at = NOW() WHERE `date` = ? AND user_id = ?";

            jdbcFunctionDao.getJdbcTemplate().batchUpdate(updateSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long timestamp = Long.valueOf(updatedItems.get(i).getDate()); // Assuming timestamp is in milliseconds
                    java.sql.Date sqlDate = new java.sql.Date(timestamp);
                    preparedStatement.setString(1, updatedItems.get(i).getLunch());
                    preparedStatement.setInt(2, user);
                    preparedStatement.setDate(3, sqlDate);
                    preparedStatement.setString(4, userId);
                }

                @Override
                public int getBatchSize() {
                    return updatedItems.size();
                }
            });
        }

        // Handle Deleted Items (DELETE or SET NULL)
        if (!deletedItems.isEmpty()) {
            String insertSql = "INSERT INTO his_lunch (lunch_id, date, lunch, user_id, created_by, created_at, updated_by, updated_at, action_type, action_by)\n" +
                    " SELECT id, date, lunch, user_id, created_by, created_at, updated_by, updated_at, 'DELETED' action_type, ? as action_by\n" +
                    "from lunch where user_id=?  and date=?";

            jdbcFunctionDao.getJdbcTemplate().batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long timestamp = Long.valueOf(deletedItems.get(i).getDate());
                    java.sql.Date sqlDate = new java.sql.Date(timestamp);
                    preparedStatement.setInt(1, user);      // action_by
                    preparedStatement.setString(2, userId); // user_id
                    preparedStatement.setDate(3, sqlDate);  // date in SELECT
                }

                @Override
                public int getBatchSize() {
                    return deletedItems.size();
                }
            });

// 2. DELETE FROM lunch table
            String deleteSql = "DELETE from lunch where  user_id=? and date=?";

            jdbcFunctionDao.getJdbcTemplate().batchUpdate(deleteSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    Long timestamp = Long.valueOf(deletedItems.get(i).getDate());
                    java.sql.Date sqlDate = new java.sql.Date(timestamp);

                    preparedStatement.setString(1, userId); // user_id in DELETE
                    preparedStatement.setDate(2, sqlDate);  // date in DELETE
                }

                @Override
                public int getBatchSize() {
                    return deletedItems.size();
                }
            });
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }



    public ResponseEntity<?> findBookedLunch(String startDate, String endDate) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userId", userService.getCurrentUserId());
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<Map<String, Object>> result = (List<Map<String, Object>>) jdbcFunctionDao.getProcedureResult(SP_GET_CALENDER, params).get("#result-set-1");
        Map<Object, Object> response = new HashMap<>();
        result.forEach(item-> response.put(item.get("date"), item));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> findLunchMenu() {
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userService.getCurrentUserId());
        return ResponseEntity.ok(jdbcFunctionDao.getProcedureResult(SP_GET_LUNCH_LIST, param).get("#result-set-1"));
    }


    public ResponseEntity<?> updateCurrentMeal(String source, String value) {
        String userId = userService.getCurrentUserId();
        Integer user = user(userId);
        if(userService.isAdmin()){
            int updateCount = jdbcFunctionDao.getJdbcTemplate().update("UPDATE conf_snacks SET `"+source+"` = ?, `updated_by` = ? WHERE `date` = ?", value,user, today());
            if(updateCount == 0){
                jdbcFunctionDao.getJdbcTemplate().update("INSERT INTO conf_snacks (`"+source+"`, `date`, `created_by`) VALUES (?, ?, ?)", value, new Date(),user);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
        }
    }


    public ResponseEntity<?> updateCurrentIdea( String value) {
        jdbcFunctionDao.getJdbcTemplate().update("INSERT INTO conf_idea (description, created_by) VALUES (?, ?)", value, userService.getCurrentUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> showCurrentIdea() {
        List<Map<String,Object>> list = jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT description AS idea FROM conf_idea ORDER BY id desc LIMIT 1");
        return new ResponseEntity<>(Objects.nonNull(list) && !list.isEmpty() ?list.get(0): Collections.emptyMap(), HttpStatus.OK);
    }



    private String today(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    private String getDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public ResponseEntity<?> updateLunch(Lunch lunch) {
        try {
            Date allowedDate = getMaxAllowedDate();
            String lunchDate = lunch.getDate();
            String currentUser = userService.getCurrentUserId();
            Integer user = user(currentUser);
            if(isNotLoggedIn(currentUser)){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
//            if((Objects.equals(lunch.getDate(), allowedDate) || lunch.getDate().after(allowedDate)) ){
//                return new ResponseEntity<>("Date Expired",HttpStatus.BAD_REQUEST);
//            }
            String sql = "INSERT INTO `lunch` (`date`, `lunch`, `user_id`,`created_by`) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE lunch = VALUES(lunch), updated_by = ?,  updated_at = NOW()";
            jdbcFunctionDao.getJdbcTemplate().update(sql, lunchDate, lunch.getLunch(), currentUser,user,user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return new ResponseEntity<>("Failed to update lunch", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> findAdminLunch(String user, String date) {

        String userQuery = "SELECT username FROM users WHERE LOWER(username) = LOWER(?) LIMIT 1";
        List<Map<String, Object>> userResult = jdbcFunctionDao.getJdbcTemplate().queryForList(userQuery, user);

        if (userResult.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        String userId = userResult.get(0).get("username").toString();

        String sql = "SELECT lunch FROM `lunch` WHERE `user_id` = ? AND `date` = ?";
       List<Map<String, Object>> list = jdbcFunctionDao.getJdbcTemplate().queryForList(sql, userId, date);
       Map<String, Object> response = new HashMap<>();
       response.put("meal", list.isEmpty() ? null: list.get(0).get("lunch"));
       return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> updateAdminLunch(String user, String lunch, String date) {
        try {
            String currentUser = userService.getCurrentUserId();
            Integer currentUserId = user(currentUser);

            // Check if the user exists
            String userQuery = "SELECT username FROM users WHERE LOWER(username) = ? LIMIT 1";
            List<Map<String, Object>> userResult = jdbcFunctionDao.getJdbcTemplate().queryForList(userQuery, user);

            if (userResult.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            String userId = userResult.get(0).get("username").toString();

            if (lunch == null || lunch.trim().isEmpty()) {
                // Insert into his_lunch table before deletion
                String insertHistorySql =
                "INSERT INTO his_lunch" +
                "(lunch_id, date, lunch, user_id, created_by, created_at, updated_by, updated_at, action_type, action_by)" +
                "SELECT id, date, lunch, user_id, created_by, created_at, updated_by, updated_at," +
                    "'DELETED' AS action_type, ? AS action_by FROM lunch WHERE user_id = ? AND date = ?";
                jdbcFunctionDao.getJdbcTemplate().update(insertHistorySql, currentUserId, userId, date);

                // Delete from lunch table
                String deleteSql = "DELETE FROM lunch WHERE user_id = ? AND date = ?";
                jdbcFunctionDao.getJdbcTemplate().update(deleteSql, userId, date);

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Insert or Update lunch when lunch is not empty
                String sql =
                "INSERT INTO lunch (`date`, `lunch`, `user_id`, `created_by`) VALUES (?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE lunch = VALUES(lunch), updated_by = ?, updated_at = NOW()";
                jdbcFunctionDao.getJdbcTemplate().update(sql, date, lunch, userId, currentUserId, currentUserId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>("Failed to update lunch", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public ResponseEntity<?> updateAdminLunch(String user, String lunch, String date) {
//        try {
//            String currentUser = userService.getCurrentUserId();
//            Integer currentUserId = user(currentUser);
////            String currentUser = userService.getCurrentUserId();
////            if(StringUtils.isEmpty(lunch.getLunch()) && (Objects.equals(lunch.getDate(), allowedDate) || lunch.getDate().after(allowedDate)) ){
////                return new ResponseEntity<>("Date Expair for update lunch",HttpStatus.BAD_REQUEST);
////            }
//            String userQuery = "SELECT username FROM users WHERE LOWER(username) = ? LIMIT 1";
//            List<Map<String, Object>> userResult = jdbcFunctionDao.getJdbcTemplate().queryForList(userQuery, user);
//
//            if (userResult.isEmpty()) {
//                Map<String, Object> errorResponse = new HashMap<>();
//                errorResponse.put("error", "User not found");
//                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//            }
//
//            String userId = userResult.get(0).get("username").toString();
//            String sql = "INSERT INTO `lunch` (`date`, `lunch`, `user_id`,`created_by`) " +
//                    "VALUES (?, ?, ?,?) " +
//                    "ON DUPLICATE KEY UPDATE lunch = VALUES(lunch), updated_by = ?, updated_at = NOW()";
//            jdbcFunctionDao.getJdbcTemplate().update(sql,date, lunch,userId,currentUserId,currentUserId);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            return new ResponseEntity<>("Failed to update lunch", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    public ResponseEntity<?> deleteMeal(String dateString) {
        if (userService.isAdmin()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            try {
                Date date = dateFormat.parse(dateString);
                String currentUser = userService.getCurrentUserId();
                Integer currentUserId = user(currentUser);
                String insertSql =
                        "INSERT INTO his_conf_lunch (conf_lunch_id, date, meal_a, meal_b, created_by, created_at, " +
                                "updated_by, updated_at, action_type, action_by) " +
                                "SELECT id, date, meal_a, meal_b, created_by, created_at, " +
                                "updated_by, updated_at, 'DELETED' as action_type, ? as action_by " +
                                "FROM conf_lunch " +
                                "WHERE date = ?";

                jdbcFunctionDao.getJdbcTemplate().update(insertSql, currentUserId, date);
                String deleteSql = "DELETE FROM conf_lunch WHERE date = ?";
                jdbcFunctionDao.getJdbcTemplate().update(deleteSql, date);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


//    public ResponseEntity<?> deleteMeal(String dateString){
//        if(userService.isAdmin()){
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
//            try {
//                Date date = dateFormat.parse(dateString);
//                jdbcFunctionDao.getJdbcTemplate().update("DELETE FROM conf_lunch WHERE `date` = ?",date );
//            } catch (Exception e) {
//                log.error(e.getMessage(), e.getCause());
//            }
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(FORBIDDEN, HttpStatus.FORBIDDEN);
//    }

    public ResponseEntity<?> configuredLunch(String year, String month) {
        if(Objects.isNull(year) || Objects.isNull(month) || year.length() != 4 || month.length() != 2){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final String sql =
                "SELECT meal_a, meal_b,\n" +
                "DATE_FORMAT(`date`, '%d %M %Y') as 'date'\n" +
                "FROM conf_lunch\n" +
                "WHERE DATE_FORMAT(`date`, '%Y-%m') = ?\n" +
                "ORDER BY `date` ASC";
        String startDate = year.concat("-").concat(month);
        String endDate = year.concat("-").concat(month).concat("-").concat("31");
        return new ResponseEntity<>(jdbcFunctionDao.getJdbcTemplate().queryForList(sql, startDate), HttpStatus.OK);
    }


    private Date getMaxAllowedDate(){

        LocalTime currentTime = LocalTime.now();
        LocalTime targetTime = LocalTime.of(10, 0);
        LocalDateTime desiredTime = currentTime.isAfter(targetTime)
                ? LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(0, 0))
                : LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0));
        return java.util.Date.from(desiredTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }

    public Integer user(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

}
