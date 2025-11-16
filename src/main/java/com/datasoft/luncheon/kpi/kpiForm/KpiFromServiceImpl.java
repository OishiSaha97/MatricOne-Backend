package com.datasoft.luncheon.kpi.kpiForm;

import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.commons.utils.QueryUtils;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class KpiFromServiceImpl implements KpiFromService {

    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;

    public KpiFromServiceImpl(JdbcFunctionDao jdbcFunctionDao, UserService userService) {
        this.jdbcFunctionDao = jdbcFunctionDao;
        this.userService = userService;
    }

    public Integer getUserId(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

    @Override
    @Transactional
    public ApiResponse saveKpi(KpiConfigParams params){
        try{
//            String userId = userService.getCurrentUserId();
//            Integer user = getUserId(userId);
            Map<String, Object> spParam = kpiPrepareParam(params);
            Map<String, Object> result = jdbcFunctionDao.getProcedureResult("sp_ins_kpi_submit", spParam);
           return new ApiResponse(HttpStatus.OK.value(),"Kpi Save Successfully",result);
        }catch (Exception e) {
//            log.error("Error FCA Team Wise kpi Submit", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    @Override
    public ApiResponse allKpiList(KpiConfigParams params) {
        try {
            Map<String, Object> param = new LinkedHashMap<>();
//            Integer user = getUserId(userId);
            param.put("userId", params.getUserIdKPI());
            param.put("pid", params.getPid());
            param.put("filterParam", QueryUtils.filterQueryBuilder(params.getFilterParam()));
            param.put("searchParam", params.getSearchParam());
            param.put("orderParam", params.getOrderParam());
            param.put("orderType", params.getOrderType());
            param.put("paramLimit", params.getParamLimit());
            param.put("paramOffset", params.getParamOffset());
            param.put("isFilterValueString", "");
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_team_kpi_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            return new ApiResponse(HttpStatus.OK.value(),"Team KPI List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }

    @Override
    public ApiResponse allEmpList(KpiConfigParams params) {
        try {
            Map<String, Object> param = new LinkedHashMap<>();
//            Integer user = getUserId(userId);
            param.put("userId", params.getUserIdKPI());
            param.put("pid", params.getPid());
            param.put("filterParam", QueryUtils.filterQueryBuilder(params.getFilterParam()));
            param.put("searchParam", params.getSearchParam());
            param.put("orderParam", params.getOrderParam());
            param.put("orderType", params.getOrderType());
            param.put("paramLimit", params.getParamLimit());
            param.put("paramOffset", params.getParamOffset());
            param.put("isFilterValueString", "");
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_all_emp_kpi_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            return new ApiResponse(HttpStatus.OK.value(),"Employee List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }

    @Override
    public ApiResponse myKpiList(KpiConfigParams params) {
        try {
            Map<String, Object> param = new LinkedHashMap<>();
//            Integer user = getUserId(userId);
            param.put("userId", params.getUserIdKPI());
            param.put("pid", params.getPid());
            param.put("filterParam", QueryUtils.filterQueryBuilder(params.getFilterParam()));
            param.put("searchParam", params.getSearchParam());
            param.put("orderParam", params.getOrderParam());
            param.put("orderType", params.getOrderType());
            param.put("paramLimit", params.getParamLimit());
            param.put("paramOffset", params.getParamOffset());
            param.put("isFilterValueString", "");
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_my_kpi_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            result.put("addPermission", procedureResult.get("#result-set-2"));
            return new ApiResponse(HttpStatus.OK.value(),"KPI List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }

    @Override
    public ApiResponse revertKpi(KpiConfigParams params) {
        try{
            Map<String, Object> spParam = kpiPrepareParam(params);
            Map<String, Object> result = jdbcFunctionDao.getProcedureResult("sp_ins_reverted_kpi_submit", spParam);
            return new ApiResponse(HttpStatus.OK.value(),"Kpi Save Successfully",result);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    @Override
    public ApiResponse evaluationInsertData(KpiConfigParams params) {
        try{
            Map<String, Object> spParam = kpiEvalustionPrepareParam(params);
            Map<String, Object> result = jdbcFunctionDao.getProcedureResult("sp_ins_evaluation_submit", spParam);
            return new ApiResponse(HttpStatus.OK.value(),"Kpi Evaluation Save Successfully",result);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    Map<String, Object> kpiPrepareParam(KpiConfigParams params) {
        Map<String, Object> spParam = new LinkedHashMap<>();
        spParam.put("param", params.getParam());
        spParam.put("randomData", params.getRandomData());
        spParam.put("year", params.getYear());
        spParam.put("objectId", params.getObjectId());
        spParam.put("userId", params.getUserIdKPI());
        spParam.put("remark", params.getRemarks());
        return spParam;
    }

    Map<String, Object> kpiEvalustionPrepareParam(KpiConfigParams params) {
        Map<String, Object> spParam = new LinkedHashMap<>();
        spParam.put("param", params.getParam());
        spParam.put("objectiveData", params.getObjectiveData());
        spParam.put("selfData", params.getSelfData());
        spParam.put("valuesData", params.getValuesData());
        spParam.put("managerData", params.getManagerData());
        spParam.put("objectId", params.getObjectId());
        spParam.put("hrData", params.getHrData());
        spParam.put("year", params.getYear());
        spParam.put("remark", params.getRemarks());
        spParam.put("pid", params.getPid());
        spParam.put("userId", params.getUserIdKPI());
        spParam.put("overallRate", params.getOtherParam());
        return spParam;
    }


}
