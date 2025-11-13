package com.datasoft.luncheon.kpi.kpiForm;


import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;


import org.springframework.stereotype.Service;


public interface KpiFromService {

    ApiResponse saveKpi(KpiConfigParams params);

    ApiResponse allKpiList(KpiConfigParams params);
    ApiResponse allEmpList(KpiConfigParams params);

    ApiResponse myKpiList(KpiConfigParams params);

    ApiResponse revertKpi(KpiConfigParams params);

    ApiResponse evaluationInsertData(KpiConfigParams params);
}
