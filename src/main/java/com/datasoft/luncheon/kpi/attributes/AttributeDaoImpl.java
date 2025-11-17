package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.kpi.attributes.dto.AttributeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AttributeDaoImpl implements AttributeDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(String selectedKpiType,String attributeName,String userId) {
        String checkSql = "SELECT COUNT(*) FROM conf_kpi_attribute " +
                "WHERE kpi_type = ? AND kpi_category_name = ?";

        Integer count = jdbcTemplate.queryForObject(
                checkSql,
                new Object[]{selectedKpiType, attributeName},
                Integer.class
        );
        if (count != null && count > 0) {
            return;
        }

        String sql = "INSERT INTO conf_kpi_attribute (kpi_type, kpi_category_name, status,created_by, created_at) VALUES (?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, selectedKpiType, attributeName,1, userId);
    }

}
