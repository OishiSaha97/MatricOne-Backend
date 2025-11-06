package com.datasoft.luncheon.commons.dao;

import com.datasoft.luncheon.commons.model.PagedResponse;
import com.datasoft.luncheon.commons.model.filter.SearchFilter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JdbcFunctionDao {

    String getFunctionResult(String functionName, Map<String, Object> param);
    Map<String, Object> getProcedureResult(String procedureName, Map<String, Object> inParam);
    PagedResponse getList(String table, SearchFilter searchFilter);

    void bulkInsert(String table, List<Map<String, Object>> data, Integer id, Integer user);

    Map<String, Object> findById(String table, Integer id);

    void update(String table, List<HashMap<String, String>> hashMaps, Map<String, Object> dataSource);
    JdbcTemplate getJdbcTemplate();
}
