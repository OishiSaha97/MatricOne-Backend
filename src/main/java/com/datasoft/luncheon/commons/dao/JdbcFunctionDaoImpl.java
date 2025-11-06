package com.datasoft.luncheon.commons.dao;

import com.datasoft.luncheon.commons.model.PagedResponse;
import com.datasoft.luncheon.commons.model.filter.SearchFilter;
import com.datasoft.luncheon.commons.utils.DatabaseUtils;
import com.datasoft.luncheon.commons.utils.QueryUtils;
import com.datasoft.luncheon.commons.model.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Repository
public class JdbcFunctionDaoImpl implements JdbcFunctionDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${DB_NAME}")
    private String dbName;

    HashSet<String> acceptedLunch = new HashSet<>(Arrays.asList("chicken", "beef", "egg", "fish", "chicken polao", "beef polao", "chicken khichuri", "beef khichuri"));

    @Override
    public String getFunctionResult(String functionName, Map<String, Object> param) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName).withCatalogName(dbName);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(param);
        return jdbcCall.executeFunction(String.class, mapSqlParameterSource);
    }

    @Override
    public Map<String, Object> getProcedureResult(String procedureName, Map<String, Object> inParam) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedureName).withCatalogName(dbName);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(inParam);
        return jdbcCall.execute(mapSqlParameterSource);
    }

    @Override
    public PagedResponse getList(String table, SearchFilter params) {
        Map<String, Object> inParam = new LinkedHashMap<>();
        inParam.put("inpUser", 1);
        inParam.put("inpTable", table);
        inParam.put("inpParam", params.getParam());
        inParam.put("inpSearch", params.getSearchParam());
        inParam.put("inpFilter", QueryUtils.filterQueryBuilder(params.getFilters()));
        inParam.put("inpSortBy", params.getSortBy());
        inParam.put("inpSortOrder", params.getSortOrder());
        inParam.put("inpLimit", params.getLimit());
        inParam.put("inpOffset", params.getOffset());
        Map<String, Object> procedureResult = getProcedureResult(DatabaseUtils.SP_GET_LIST, inParam);
        List<Map<String, Object>> list = (List<Map<String, Object>>) procedureResult.getOrDefault("#result-set-1", new ArrayList<>());
        Integer totalData = (Integer) procedureResult.get("totalData");
        return new PagedResponse(list, totalData);

    }

//    @Override
//    public void bulkInsert(String table, List<Map<String, Object>> dataList, Integer id,Integer user) {
//        List<HashMap<String, String>> columnMappings = DatabaseUtils.columnMappings(table);
//
//        try {
//            for (Map<String, Object> data : dataList) {
//                StringBuilder sqlQuery = new StringBuilder("INSERT INTO `" + table + "` (");
//                int parameterIndex = 1;
//
//
//                for (Map<String, String> mapping : columnMappings) {
//                    String databaseKey = mapping.get(Strings.KEY_DB);
//                    sqlQuery.append("`").append(databaseKey.replace(" ", "_")).append("`").append(", ");
//                }
//
//                sqlQuery.delete(sqlQuery.length() - 2, sqlQuery.length());
//                sqlQuery.append(") VALUES (");
//
//                for (Map.Entry<String, Object> entry : data.entrySet()) {
//                    sqlQuery.append("?, ");
//                }
//                //sqlQuery.append("?, ");
//
//                sqlQuery.delete(sqlQuery.length() - 2, sqlQuery.length());
//                sqlQuery.append(")");
//                if(table.equals("conf_lunch")){
//
//                    sqlQuery.append("ON DUPLICATE KEY UPDATE ");
//                    sqlQuery.append("meal_a = VALUES(meal_a), ");
//                    sqlQuery.append("meal_b = VALUES(meal_b)");
//                }
//                try (Connection connection = jdbcTemplate.getDataSource().getConnection();
//                     PreparedStatement ps = connection.prepareStatement(sqlQuery.toString())) {
//                    parameterIndex = 1;
//                    for(HashMap<String, String> mapping : columnMappings){
//                        String value = Objects.nonNull(data.get(mapping.get(Strings.KEY_DB))) ? data.get(mapping.get(Strings.KEY_DB)).toString() : null;
//                        if(Objects.nonNull(value)){
//                            switch (mapping.get(Strings.KEY_DATATYPE).toUpperCase()) {
//                                case "STRING":{
//                                    if(table.equals("conf_lunch")){
//                                      if(!acceptedLunch.contains(value.trim().toLowerCase())){
//                                          throw new RuntimeException("Invalid Meal: "+value+", Please ensure valid spelling and name");
//
//                                      }
//                                    }
//                                    ps.setString(parameterIndex++, value);
//                                    break;
//                                }
//                                case "DATE":{
//                                    Date utilDate = null;
//                                    try {
//                                        utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(value.trim());
//                                    } catch (ParseException e) {
//                                        throw new RuntimeException("Invalid date value: " + value, e);
//                                    }
//                                    ps.setDate(parameterIndex++, new java.sql.Date(utilDate.getTime()));
//                                    break;
//                                }
//                                case "DOUBLE":{
//                                    if (value == null || value.trim().isEmpty()) {
//                                        ps.setDouble(parameterIndex++, 0);
//
//                                    } else {
//                                        try {
//                                            String[] parts = value.replace(",", "").split("\\.");
//                                            int integralPart = Integer.parseInt(parts[0]);
//                                            int fractionalPart = Integer.parseInt(parts[1]);
//                                            double doubleValue = integralPart + (fractionalPart / 100.0);
//                                            ps.setDouble(parameterIndex++, doubleValue);
//                                        } catch (NumberFormatException e) {
//                                            throw new RuntimeException("Invalid double value: " + value, e);
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                        } else {
//                            ps.setObject(parameterIndex++, null);
//                        }
//
//                    }
//
//                    ps.executeUpdate();
//                } catch (SQLException e) {
//                    throw new RuntimeException("Error executing SQL query for bulk insert: " + e.getMessage(), e);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    @Override
    public void bulkInsert(String table, List<Map<String, Object>> dataList, Integer id, Integer userId) {
        List<HashMap<String, String>> columnMappings = DatabaseUtils.columnMappings(table);

        try {
            for (Map<String, Object> data : dataList) {
                StringBuilder sqlQuery = new StringBuilder("INSERT INTO `" + table + "` (");

                // Add existing columns dynamically
                for (Map<String, String> mapping : columnMappings) {
                    String databaseKey = mapping.get(Strings.KEY_DB);
                    sqlQuery.append("`").append(databaseKey.replace(" ", "_")).append("`, ");
                }

                // Append the `created_by` column to the query
                sqlQuery.append("`created_by`");

                sqlQuery.append(") VALUES (");

                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    sqlQuery.append("?, ");
                }

                // Append placeholder for `created_by`
                sqlQuery.append("?");

                sqlQuery.append(")");

                if (table.equals("conf_lunch")) {
                    sqlQuery.append(" ON DUPLICATE KEY UPDATE ");
                    sqlQuery.append("meal_a = VALUES(meal_a), ");
                    sqlQuery.append("meal_b = VALUES(meal_b)");
                }

                try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                     PreparedStatement ps = connection.prepareStatement(sqlQuery.toString())) {
                    int parameterIndex = 1;

                    for (HashMap<String, String> mapping : columnMappings) {
                        String value = Objects.nonNull(data.get(mapping.get(Strings.KEY_DB))) ? data.get(mapping.get(Strings.KEY_DB)).toString() : null;

                        if (Objects.nonNull(value)) {
                            switch (mapping.get(Strings.KEY_DATATYPE).toUpperCase()) {
                                case "STRING":
                                    ps.setString(parameterIndex++, value);
                                    break;
                                case "DATE":
                                    Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(value.trim());
                                    ps.setDate(parameterIndex++, new java.sql.Date(utilDate.getTime()));
                                    break;
                                case "DOUBLE":
                                    ps.setDouble(parameterIndex++, Double.parseDouble(value));
                                    break;
                                default:
                                    ps.setObject(parameterIndex++, value);
                            }
                        } else {
                            ps.setObject(parameterIndex++, null);
                        }
                    }

                    // Set the `created_by` parameter with `userId`
                    ps.setInt(parameterIndex++, userId);

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException("Error executing SQL query for bulk insert: " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Map<String, Object> findById(String table, Integer id) {
        Map<String, Object> inParam = new LinkedHashMap<>();
        inParam.put("inpUser", 1);
        inParam.put("inpTable", table);
        inParam.put("inpParam", null);
        inParam.put("inpSearch", null);
        inParam.put("inpFilter", "`id`="+id);
        inParam.put("inpSortBy", null);
        inParam.put("inpSortOrder", null);
        inParam.put("inpLimit", 1);
        inParam.put("inpOffset", 0);
        Map<String, Object> procedureResult = getProcedureResult(DatabaseUtils.SP_GET_LIST, inParam);
        List<Map<String, Object>> list = (List<Map<String, Object>>) procedureResult.getOrDefault("#result-set-1", new ArrayList<>());
        return Objects.nonNull(list) && !list.isEmpty()? list.get(0):Collections.EMPTY_MAP;
    }

    @Override
    public void update(String table, List<HashMap<String, String>> mappings, Map<String, Object> dataSource) {
        StringBuilder sqlQuery = new StringBuilder("UPDATE `" + table + "` SET ");

        for (HashMap<String, String> mapping : mappings) {
            String databaseKey = mapping.get(Strings.KEY_DB);
            sqlQuery.append("`").append(databaseKey.replace(" ", "_")).append("`").append(" = ?, ");
        }
        sqlQuery.delete(sqlQuery.length() - 2, sqlQuery.length());

        sqlQuery.append(" WHERE `id` = ?");

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery.toString())) {
            int parameterIndex = 1;
            for (HashMap<String, String> mapping : mappings) {
                String key = mapping.get(Strings.KEY_DB);
                Object value = dataSource.get(key);
                if (!key.equalsIgnoreCase("id")) {
                    ps.setObject(parameterIndex++, value);
                }
            }
            ps.setInt(parameterIndex, (Integer) dataSource.get("id"));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL update query: " + e.getMessage(), e);
        }
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}