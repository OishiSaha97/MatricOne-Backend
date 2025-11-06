package com.datasoft.luncheon.kpi.dashboard;


import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.user.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<?> saveDateForkpi(String date, String userId) {
        dashboardDao.saveKpiDate(date,userId);
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

}
