package com.datasoft.luncheon.kpi.dashboard;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kpi/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/save/endDate")
    public ResponseEntity<?> saveDateForkpi( @RequestParam(required = false) String date,
                                                @RequestParam(required = false) String userId,
                                             @RequestParam(required = false) String forDate) {
        return dashboardService.saveDateForkpi(date,userId,forDate);
    }

    @PostMapping("/save/evalutionDate")
    public ResponseEntity<?> evalutionDateForkpi( @RequestParam(required = false) String date,
                                                @RequestParam(required = false) String userId,
                                             @RequestParam(required = false) String forDate) {
        return dashboardService.evalutionDateForkpi(date,userId,forDate);
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


    @PostMapping("/save/announcement")
    public ResponseEntity<?> announcement(@RequestParam(required = false) String userId,@RequestParam(required = false) String remarkData) {
        return dashboardService.announcement(userId,remarkData);
    }

    @PostMapping(value = "/notification")
    public ApiResponse getNotification(@RequestBody KpiConfigParams params) {
        return dashboardService.getNotification(params);
    }


}
