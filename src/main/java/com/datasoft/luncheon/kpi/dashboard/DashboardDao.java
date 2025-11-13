package com.datasoft.luncheon.kpi.dashboard;

public interface DashboardDao {
    void saveKpiDate(String date, String userId, String forDate);

    void saveKpiDateForSpecific(String date, String userId, String teamName, String userKpiId);

    void evalutionKpiDate(String date, String userId, String forDate);

    void announcementSave(String userId, String remarkData);

    void saveInitiationNotification(String date, String userId, String forDate);

    void saveEvalutionDateNotification(String date, String userId, String forDate);
}
