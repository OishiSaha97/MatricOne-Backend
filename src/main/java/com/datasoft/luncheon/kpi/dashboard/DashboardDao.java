package com.datasoft.luncheon.kpi.dashboard;

public interface DashboardDao {
    void saveKpiDate(String date, String userId);

    void saveKpiDateForSpecific(String date, String userId, String teamName, String userKpiId);
}
