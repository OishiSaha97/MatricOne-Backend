package com.datasoft.luncheon.commons.utils;


import java.util.*;

import static com.datasoft.luncheon.commons.model.Strings.*;


public class DatabaseUtils {

    public static final String TABLE_HOLIDAY = "conf_holiday";
    public static final String TABLE_LUNCH = "conf_lunch";

    public static final String TABLE_FEEDBACK = "feedback";

    public static final String SP_GET_LIST = "sp_get_list";
    public static final String SP_GET_MEAL_VIEW = "sp_get_list";
    public static final String sp_get_config = "sp_get_config";
    public static final String SP_GET_CALENDER = "sp_get_calender";
    public static final String SP_GET_LUNCH_LIST = "sp_get_lunch_list";

    public static List<HashMap<String, String>> columnMappings(String tableName){
        switch (tableName) {

            case TABLE_HOLIDAY: return Arrays.asList(
                createMapping("Date", "event_date", "date", "required"),
                createMapping("Name", "event_name", "string", "required"),
                createMapping("Description", "event_description", "string", "required")
            );

            case TABLE_FEEDBACK: return Arrays.asList(
                    createMapping("User Name", "created_by", "date", "required"),
                    createMapping("Feedback", "details", "string", "required"),
                    createMapping("Ratings", "ratings", "number", "required")
            );

            case TABLE_LUNCH: return Arrays.asList(
                createMapping("Date", "date", "date", "required"),
                createMapping("Meal A", "meal_a", "string", "required"),
                createMapping("Meal B", "meal_b", "string", "required")
            );
            case "BILL": return Arrays.asList(
                createMapping("Team", "team", "string", "required"),
                createMapping("ID", "empId", "string", "required"),
                createMapping("Name", "employee", "string", "required"),
                createMapping("Self Lunch", "self", "string", "required"),
                createMapping("Guest Lunch", "guest", "string", "required"),
                createMapping("Total Lunch", "numberOfLunch", "string", "required"),
                createMapping("Amount (BDT)", "totalBill", "string", "required")
            );
            case "LUNCH": return Arrays.asList(
                    createMapping("Team", "team", "string", "required"),
                    createMapping("ID", "empId", "string", "required"),
                    createMapping("Name", "employee", "string", "required"),
                    createMapping("Self Lunch", "self", "string", "required"),
                    createMapping("Guest Lunch", "guest", "string", "required"),
                    createMapping("Total Lunch", "numberOfLunch", "string", "required"),
                    createMapping("Self Lunch", "selfLunch", "string", "required"),
                    createMapping("Guest Lunch", "guestLunch", "string", "required")

//                    createMapping("Amount (BDT)", "totalBill", "string", "required")
            );
            default: return new ArrayList<>();
        }
    }

    private static HashMap<String, String> createMapping(String excelKey, String dbKey, String dataType, String isRequired) {
        return new HashMap<String, String>(){{
            put(KEY_EXCEL, excelKey);
            put(KEY_DB, dbKey);
            put(KEY_DATATYPE, dataType);
            put("isRequired", isRequired);
        }};
    }

    public static String getTableName(String source) {
        switch (source) {
            case "holiday": return TABLE_HOLIDAY;
            case "lunch": return TABLE_LUNCH;
            case "feedback": return TABLE_FEEDBACK;
            default: return  "";
        }
    }
}
