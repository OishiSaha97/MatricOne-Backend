package com.datasoft.luncheon.kpi.hierarchy;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kpi/hierarchy")
public class HierarchyController {


    private final HierarchyService hierarchyService;

    public HierarchyController(HierarchyService hierarchyService) {
        this.hierarchyService = hierarchyService;
    }

    @PostMapping("/save")
    public ApiResponse submitKpi(@RequestBody KpiConfigParams params) {

        return hierarchyService.saveHierarchy(params);
    }

    @PostMapping(value = "/list")
    public ApiResponse allHierarchy(@RequestBody KpiConfigParams params) {
        return hierarchyService.allHierarchy(params);
    }

    @PostMapping("/config/data")
    public ResponseEntity<?> getConfigItem(@RequestBody KpiConfigParams params){
        return hierarchyService.getConfigItem(params);
    }

    @PostMapping("/add/final_approver")
    public ResponseEntity<?> addFinalApprover(
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String approverId,
                                      @RequestParam(required = false) String userId
    ){
        return hierarchyService.addFinalApprover(name,userId,approverId);
    }

}
