package com.datasoft.luncheon.lunchbill;

import com.datasoft.luncheon.commons.ExcelProcessorService;
import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.utils.DatabaseUtils;
import com.datasoft.luncheon.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LunchBillService {
    private final JdbcFunctionDao jdbcFunctionDao;
    private final ExcelProcessorService excelProcessorService;
    private final UserService userService;


    public void downloadLunch(String users,HttpServletResponse response){
        if(userService.isAdmin()){
            sendLunchExcel(users,response);
        }
    }

    private void sendLunchExcel(String users, HttpServletResponse response) {
        excelProcessorService.writeExcel(
                this.getLunchList(users),
                DatabaseUtils.columnMappings("LUNCH"),
                response
        );
    }

    public void downloadMyBill(String year, String month, HttpServletResponse response) {
        sendMyBillExcel(year, month, response, userService.getCurrentUserId());
    }

    private void sendMyBillExcel(String year, String month, HttpServletResponse response, String user){
        excelProcessorService.writeExcel(
                this.getLunchBill(year, month, userService.getCurrentUserId()),
                DatabaseUtils.columnMappings("BILL"),
                response
        );
    }


    public void downloadBill(String year, String month,String users, HttpServletResponse response){
        if(userService.isAdmin()){
            sendBillExcel(year, month, response, users);
        }
    }

    private void sendBillExcel(String year, String month, HttpServletResponse response, String users){
        excelProcessorService.writeExcel(
                this.getLunchBill(year, month, users),
                DatabaseUtils.columnMappings("BILL"),
                response
        );
    }

    public List<Map<String, Object>> getLunchBill(String year, String month, String users){
        String startDate = year+"-"+month+"-"+"01";
        String endDate = year+"-"+month+"-"+"31";
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("userIds", users);
        return (List<Map<String, Object>>) jdbcFunctionDao.getProcedureResult("sp_get_lunch_bill", param).get("#result-set-1");
    }

    public List<Map<String, Object>> getLunchList(String users){
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("userIds", users);
        return (List<Map<String, Object>>) jdbcFunctionDao.getProcedureResult("sp_report",param).get("#result-set-1");
    }

    public List<Map<String, Object>> getVendorList(String users, String date){
        Map<String, Object> param = new LinkedHashMap<>();
        param.put("userIds", users);
        param.put("pDate", date);
//        List<Map<String,Object>> list = jdbcFunctionDao.getJdbcTemplate().queryForList("SELECT username FROM users WHERE LOWER(username) = ? LIMIT 1", users);
        return (List<Map<String, Object>>) jdbcFunctionDao.getProcedureResult("sp_vendor_report",param).get("#result-set-1");
    }


}