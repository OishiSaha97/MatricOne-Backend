package com.datasoft.luncheon.kpi.dashboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DashboardDaoImpl implements DashboardDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void saveKpiDate(String date, String userId, String forDate) {
        String updateSql = "UPDATE conf_kpi_date SET status = 0  ";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateSql);
        int currentYear = LocalDate.now().getYear();

        String sql = "INSERT INTO conf_kpi_date (kpi_last_date, status, year,kpi_date_for, created_by) VALUES (?, 1, ?, ?, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sql,date,currentYear,forDate,userId);
    }

    @Override
    public void saveKpiDateForSpecific(String date, String userId, String teamName, String userKpiId) {
        String sql = "INSERT INTO conf_kpi_date_specific " +
                "(kpi_last_date_uni, name, user_id, team_name, year, status, created_by, created_at) " +
                "VALUES (?, ?, ?, ?, ?, 1, ?, NOW())";

        // Use jdbcTemplate to execute the insert
        namedParameterJdbcTemplate.getJdbcTemplate().update(
                sql,
                date,
                userKpiId,
                userId,
                teamName,
                LocalDate.now().getYear(),
                userKpiId
        );
    }

    @Override
    public void evalutionKpiDate(String date, String userId, String forDate) {
        String updateSql = "UPDATE conf_kpi_date_evalution SET status = 0  ";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateSql);
        int currentYear = LocalDate.now().getYear();

        String sql = "INSERT INTO conf_kpi_date_evalution (kpi_last_date, status, year,kpi_date_for, created_by) VALUES (?, 1, ?, ?, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sql,date,currentYear,forDate,userId);
    }

    @Override
    public void announcementSave(String userId, String remarkData) {
        String sql = "INSERT INTO announcements (announcement, created_by) VALUES (?, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sql,remarkData,userId);
    }

    @Override
    public void saveInitiationNotification(String date, String userId, String forDate) {
        String getInitiationDateSql =
                "SELECT IFNULL(kpi_last_date, '') " +
                        "FROM conf_kpi_date " +
                        "WHERE `year` = YEAR(NOW()) " +
                        "AND `status` = TRUE " +
                        "LIMIT 1";

        String getInitiationDate = "";
        try {
            getInitiationDate = jdbcTemplate.queryForObject(getInitiationDateSql, String.class);
        } catch (EmptyResultDataAccessException e) {
            getInitiationDate = "";
        }

        // Decide which notification message to use
        String notification;
        if (getInitiationDate == null || getInitiationDate.isEmpty()) {
            notification = "The KPI initiation window is now open. Please submit your KPIs by "
                    + date + ".";
        } else {
            notification = "The KPI initiation end date has been changed to " + date
                    + ". Please ensure your submissions are completed before the deadline.";
        }

        // Insert into notifications table
        String insertSql =
                "INSERT INTO notifications (notification, notification_for, created_by) " +
                        "VALUES (:notification, 'all', :userId)";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("notification", notification);
        params.put("userId", userId);

        namedParameterJdbcTemplate.update(insertSql, params);
    }

    @Override
    public void saveEvalutionDateNotification(String date, String userId, String forDate) {
        String getInitiationDateSql =
                "SELECT IFNULL(kpi_last_date, '') " +
                        "FROM conf_kpi_date_evalution " +
                        "WHERE `year` = YEAR(NOW()) " +
                        "AND `status` = TRUE " +
                        "LIMIT 1";

        String getInitiationDate = "";
        try {
            getInitiationDate = jdbcTemplate.queryForObject(getInitiationDateSql, String.class);
        } catch (EmptyResultDataAccessException e) {
            getInitiationDate = "";
        }

        // Decide which notification message to use
        String notification;
        if (getInitiationDate == null || getInitiationDate.isEmpty()) {
            notification = "The KPI initiation window is now open. Please complete your evaluations by  "
                    + date + ".";
        } else {
            notification = "The KPI evaluation end date has been changed to " + date
                    + ". Please ensure your submissions are completed before the deadline.";
        }

        // Insert into notifications table
        String insertSql =
                "INSERT INTO notifications (notification, notification_for, created_by) " +
                        "VALUES (:notification, 'all', :userId)";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("notification", notification);
        params.put("userId", userId);

        namedParameterJdbcTemplate.update(insertSql, params);
    }

    @Override
    public void updateEvaDate(String date, String userId, String forDate) {
        String updateSql = "UPDATE conf_kpi_date_evalution SET status = 0 WHERE `year` = YEAR(NOW())  ";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateSql);
        int currentYear = LocalDate.now().getYear();
    }


}

