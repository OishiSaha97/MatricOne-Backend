package com.datasoft.luncheon.kpi.hierarchy;

import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.commons.utils.QueryUtils;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class HierarchyServiceImpl implements HierarchyService {


    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;
    private final HierarchyDao hierarchyDao;


    public HierarchyServiceImpl(JdbcFunctionDao jdbcFunctionDao, UserService userService, HierarchyDao hierarchyDao) {
        this.jdbcFunctionDao = jdbcFunctionDao;
        this.userService = userService;
        this.hierarchyDao = hierarchyDao;
    }

    public Integer getUserId(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

    @Override
    public ApiResponse saveHierarchy(KpiConfigParams params){
        try{
            Map<String, Object> spParam = kpiPrepareParam(params );
            Map<String, Object> result = jdbcFunctionDao.getProcedureResult("sp_ins_hierarchy_submit", spParam);
            return new ApiResponse(HttpStatus.OK.value(),"Approval Hierarchy added successfully",result);}catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getCause());
        }
    }

    @Override
    public ApiResponse allHierarchy(KpiConfigParams params) {
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
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_hierarchy_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            return new ApiResponse(HttpStatus.OK.value(),"Hierarchy List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }



    Map<String, Object> kpiPrepareParam(KpiConfigParams params) {
        Map<String, Object> spParam = new LinkedHashMap<>();
        spParam.put("param", params.getParam());
        spParam.put("objectId", params.getObjectId());
        spParam.put("approver", params.getApprover());
        spParam.put("team", params.getTeam());
        spParam.put("userId", params.getUserIdKPI());
        spParam.put("hierarchyData", params.getHierarchyData());
//        spParam.put("year", params.getYear());
//        spParam.put("userId", userId);
        return spParam;
    }

    @Override
    public ResponseEntity<?> getConfigItem(KpiConfigParams params) {
        try {
            Map<String, Object> spParam = prepareParamItem(params);

            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_config_sub_list", spParam);
            HashMap<String, Object> resultMap = new HashMap<>();
            String sequenceOfList = procedureResult.get("sequenceOfList").toString();
            String[] resultSize = sequenceOfList.split("~");

            for (int i = 0; i < resultSize.length; i++) {
                resultMap.put(resultSize[i], procedureResult.get("#result-set-" + (i + 1)));
            }
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (Exception ex){
            log.error("Config item fetch failed - unknown error {}", params, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public ResponseEntity<?> addFinalApprover( String name,String userId,String approverId) {
        hierarchyDao.saveFinalApproverData(name,userId,approverId);
        return new ResponseEntity<>(new ApiResponse(200, "Final Approver Updated successfully", null), HttpStatus.OK);
    }

    Map<String, Object> prepareParamItem(KpiConfigParams params) throws Exception {
        Map<String, Object> spParam = new LinkedHashMap<>();
        spParam.put("userId", params.getUserIdKPI());
        spParam.put("pid", params.getPid());
        spParam.put("objectId", params.getObjectId());
        spParam.put("segment", params.getParam());
        spParam.put("accountNumber", params.getAccountNumber());
        spParam.put("parameter", params.getParameter());
        spParam.put("paramLimit", params.getParamLimit());
        spParam.put("paramOffset", params.getParamOffset());
        spParam.put("extraParam", params.getExtraParam());
        return spParam;
    }


}

