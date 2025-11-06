package com.datasoft.luncheon.kpi.dashboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class DashboardDaoImpl implements DashboardDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public void saveKpiDate(String date, String userId) {
        String updateSql = "UPDATE conf_kpi_date SET status = 0";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateSql);
        int currentYear = LocalDate.now().getYear();

        String sql = "INSERT INTO conf_kpi_date (kpi_last_date, status, year, created_by) VALUES (?, 1, ?, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sql,date,currentYear,userId);
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

}

