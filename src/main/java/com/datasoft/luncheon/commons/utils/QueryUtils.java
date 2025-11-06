package com.datasoft.luncheon.commons.utils;

import com.datasoft.luncheon.commons.model.filter.SingleFilter;

import java.util.List;

public class QueryUtils {

    public static String filterQueryBuilder(List<SingleFilter> filters)  {
        if (filters == null || filters.isEmpty()) return "";
        StringBuilder generatedQuery = new StringBuilder("(");
        for (SingleFilter filter : filters) {
            generatedQuery
                    .append(filter.getPrefix().equalsIgnoreCase("WHERE") ? "": filter.getPrefix().toUpperCase())
                    .append(" ")
                    .append("`").append(filter.getKey()).append("`")
                    .append(" ")
                    .append(filter.getCompare().toUpperCase())
                    .append(" ")
                    .append(formatFilterValue(filter.getDataType(), filter.getValue()))
            ;
            if(filter.getCompare().equalsIgnoreCase("BETWEEN")){
                generatedQuery.append(" AND ").append(formatFilterValue(filter.getDataType(), filter.getValue2()));
            }
        }
        return generatedQuery.append(" )").toString();
    }

    private static String formatFilterValue(String type, String value) {
        switch (type.toUpperCase()) {
            case "STRING":
                value = "'" + value.replace("'","\\'").trim() + "'";
                break;

            case "DATE":
                value = "DATE_FORMAT('" + value + "','%y-%m-%d')";
                break;
        }
        return value;
    }

}
