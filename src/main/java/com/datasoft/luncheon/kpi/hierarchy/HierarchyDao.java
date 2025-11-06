package com.datasoft.luncheon.kpi.hierarchy;

import com.datasoft.luncheon.kpi.KpiConfigParams;

import java.util.Map;

public interface HierarchyDao {

    void saveFinalApproverData( String name, String userId, String approverId);
}
