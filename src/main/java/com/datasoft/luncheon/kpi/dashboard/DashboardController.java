package com.datasoft.luncheon.kpi.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kpi/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/save/endDate")
    public ResponseEntity<?> saveDateForkpi( @RequestParam(required = false) String date,
                                                @RequestParam(required = false) String userId) {
        return dashboardService.saveDateForkpi(date,userId);
    }

    @PostMapping("/add/dateSet")
    public ResponseEntity<?> addSetDate(
            @RequestParam(required = false) String modifyFor,
            @RequestParam(required = false) String selectedFor,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String userKpiId
    ){
        return dashboardService.addSetDate(modifyFor,date,selectedFor,userKpiId);
    }


}
