package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.kpi.attributes.dto.AttributeDto;

public interface AttributeDao {

    void save(String selectedKpiType,String attributeName,String userId);

    void update(String selectedKpiType, String attributeName, String userId, String id);
}
