package com.datasoft.luncheon.kpi.kpiForm;


import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kpi")
public class KpiFormController {



    private final KpiFromService kpiFromService;

    public KpiFormController(KpiFromService kpiFromService) {
        this.kpiFromService = kpiFromService;
    }

    @PostMapping("/kpi-form/save")
    public ApiResponse submitKpi(@RequestBody KpiConfigParams params) {

        return kpiFromService.saveKpi(params);
    }

    @PostMapping("/kpi-form/revert")
    public ApiResponse revertKpi(@RequestBody KpiConfigParams params) {

        return kpiFromService.revertKpi(params);
    }

    @PostMapping(value = "/list/myKpi")
    public ApiResponse myKpiList(@RequestBody KpiConfigParams params) {
        return kpiFromService.myKpiList(params);
    }


    @PostMapping(value = "/list/teamKpi")
    public ApiResponse allKpiList(@RequestBody KpiConfigParams params) {
        return kpiFromService.allKpiList(params);
    }


    @PostMapping(value = "/list/allEmp")
    public ApiResponse allEmpList(@RequestBody KpiConfigParams params) {
        return kpiFromService.allEmpList(params);
    }

    @PostMapping(value = "/kpi-form/evaluation-insert")
    public ApiResponse evaluationInsertData(@RequestBody KpiConfigParams params) {

        return kpiFromService.evaluationInsertData(params);
    }


}
