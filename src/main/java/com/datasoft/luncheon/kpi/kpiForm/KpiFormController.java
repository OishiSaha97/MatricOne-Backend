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

    @PostMapping(value = "/list/myKpi")
    public ApiResponse myKpiList(@RequestBody KpiConfigParams params) {
        return kpiFromService.myKpiList(params);
    }

    @PostMapping(value = "/list/teamKpi")
    public ApiResponse allKpiList(@RequestBody KpiConfigParams params) {
        return kpiFromService.allKpiList(params);
    }


}
