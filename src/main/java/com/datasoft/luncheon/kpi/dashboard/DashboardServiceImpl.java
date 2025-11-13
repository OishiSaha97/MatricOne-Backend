package com.datasoft.luncheon.kpi.dashboard;


import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.commons.utils.QueryUtils;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.user.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;
    private final DashboardDao dashboardDao;

    public DashboardServiceImpl(JdbcFunctionDao jdbcFunctionDao, UserService userService, DashboardDao dashboardDao) {
        this.jdbcFunctionDao = jdbcFunctionDao;
        this.userService = userService;
        this.dashboardDao = dashboardDao;
    }


    @Override
    public ResponseEntity<?> saveDateForkpi(String date, String userId, String forDate) {
        dashboardDao.saveInitiationNotification(date,userId,forDate);
        dashboardDao.saveKpiDate(date,userId,forDate);
        return new ResponseEntity<>(new ApiResponse(200, "KPI Date configured successfully", null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addSetDate(String modifyFor, String date, String selectedFor, String userKpiId) {
// came here with for whose modified will be applicable as team, individual,all -> selectedFor
        //for applicable team name, person userid -> modifyFor
        try {
            List<String> modifyForList = new ArrayList<>();

            if (selectedFor != null && !selectedFor.equalsIgnoreCase("All Employees")) {
                modifyForList = new ObjectMapper().readValue(modifyFor, new TypeReference<List<String>>() {});
                for (String item : modifyForList) {
                    dashboardDao.saveKpiDateForSpecific(date, userKpiId, item, userKpiId);
                }
            }

            return new ResponseEntity<>(new ApiResponse(200, "KPI Date configured successfully", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(500, "Error saving KPI Date", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> evalutionDateForkpi(String date, String userId, String forDate) {
        dashboardDao.saveEvalutionDateNotification(date,userId,forDate);
        dashboardDao.evalutionKpiDate(date,userId,forDate);
        return new ResponseEntity<>(new ApiResponse(200, "KPI Date configured successfully", null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> announcement(String userId, String remarkData) {
        dashboardDao.announcementSave(userId,remarkData);
        return new ResponseEntity<>(new ApiResponse(200, "KPI Date configured successfully", null), HttpStatus.OK);
    }

    @Override
    public ApiResponse getNotification(KpiConfigParams params) {
        try {
            Map<String, Object> param = new LinkedHashMap<>();
//            Integer user = getUserId(userId);
            param.put("userId", params.getUserIdKPI());
            param.put("searchParam", params.getSearchParam());
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_notification_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            return new ApiResponse(HttpStatus.OK.value(),"Notification List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }


}
