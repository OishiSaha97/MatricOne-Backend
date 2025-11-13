package com.datasoft.luncheon.kpi.dashboard;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import org.springframework.http.ResponseEntity;

public interface DashboardService {
    ResponseEntity<?> saveDateForkpi(String date, String userId, String forDate);

    ResponseEntity<?> addSetDate(String modifyFor, String date, String selectedFor, String userKpiId);

    ResponseEntity<?> evalutionDateForkpi(String date, String userId, String forDate);

    ResponseEntity<?> announcement(String userId, String remarkData);

    ApiResponse getNotification(KpiConfigParams params);
}
