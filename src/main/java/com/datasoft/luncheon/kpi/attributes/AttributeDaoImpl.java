package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.commons.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeDaoImpl implements AttributeDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ApiResponse save(String selectedKpiType, String attributeName, String userId) {
        String checkSql = "SELECT COUNT(*) FROM conf_kpi_attribute " +
                "WHERE kpi_type = ? AND kpi_category_name = ?";

        Integer count = jdbcTemplate.queryForObject(
                checkSql,
                new Object[]{selectedKpiType, attributeName},
                Integer.class
        );
        if (count != null && count > 0) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "KPI Type had already same category name", null);
        }
        else{
            String sql = "INSERT INTO conf_kpi_attribute (kpi_type, kpi_category_name, status,created_by, created_at) VALUES (?, ?, ?, ?, NOW())";
            jdbcTemplate.update(sql, selectedKpiType, attributeName,1, userId);
            return new ApiResponse(HttpStatus.OK.value(), "Created Succesfully", null);
        }

    }

    @Override
    public ApiResponse update(String selectedKpiType, String attributeName, String userId, String id) {

        String checkSql = "SELECT COUNT(*) FROM conf_kpi_attribute " +
                "WHERE kpi_type = ? AND kpi_category_name = ?";

        Integer count = jdbcTemplate.queryForObject(
                checkSql,
                new Object[]{selectedKpiType, attributeName},
                Integer.class
        );
        if (count != null && count > 0) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "KPI Type had already same category name", null);
        }else{
            String sql = "UPDATE conf_kpi_attribute " +
                    "SET kpi_type = ?, " +
                    "    kpi_category_name = ?, " +
                    "    updated_by = ?, " +
                    "    updated_at = NOW() " +
                    "WHERE id = ?";

            jdbcTemplate.update(sql, selectedKpiType, attributeName, userId, id);
            return new ApiResponse(HttpStatus.OK.value(), "Updated Succesfully", null);
        }

    }

}
