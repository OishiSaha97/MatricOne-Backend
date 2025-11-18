package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.commons.model.ApiResponse;

public interface AttributeDao {

    ApiResponse save(String selectedKpiType, String attributeName, String userId);

    void update(String selectedKpiType, String attributeName, String userId, String id);
}
