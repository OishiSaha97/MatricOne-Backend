package com.datasoft.luncheon.kpi.hierarchy;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import org.springframework.http.ResponseEntity;

public interface HierarchyService {

    ApiResponse saveHierarchy(KpiConfigParams params);

    ApiResponse allHierarchy(KpiConfigParams params);

    ResponseEntity<?> getConfigItem(KpiConfigParams params);

    ResponseEntity<?> addFinalApprover(String name,String userId,String approverId);
}
