package com.datasoft.luncheon.kpi.dashboard;

import org.springframework.http.ResponseEntity;

public interface DashboardService {
    ResponseEntity<?> saveDateForkpi(String date, String userId);

    ResponseEntity<?> addSetDate(String modifyFor, String date, String selectedFor, String userKpiId);
}
