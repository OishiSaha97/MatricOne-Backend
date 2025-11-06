package com.datasoft.luncheon.kpi.hierarchy;

import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HierarchyDaoImpl implements HierarchyDao {


    private final JdbcFunctionDao jdbcFunctionDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public HierarchyDaoImpl(JdbcFunctionDao jdbcFunctionDao) {
        this.jdbcFunctionDao = jdbcFunctionDao;
    }

    @Override
    public void saveFinalApproverData( String name, String userId, String approverId) {

        String updateSql = "UPDATE conf_final_approver SET status = 0";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateSql);

        String updateFinalHierSql = "UPDATE hierarchy SET final_approver = CONCAT('{user_name=', ?, ', full_name=', ?, '}')";
        namedParameterJdbcTemplate.getJdbcTemplate().update(updateFinalHierSql, approverId, name);

        String sql = "INSERT INTO conf_final_approver (approver_name,approver_id, status, created_by) VALUES (?, ?, 1, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sql,name,approverId,userId);

    }

}
