package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.kpi.attributes.dto.AttributeDto;
import org.springframework.http.ResponseEntity;

public interface AttributeService {


    ApiResponse allAttribute(KpiConfigParams params);

    ResponseEntity<?> saveAttribute(String selectedKpiType,String attributeName,String userId);

    ResponseEntity<?> updateAttribute(String selectedKpiType, String attributeName, String userId,String id);
}
