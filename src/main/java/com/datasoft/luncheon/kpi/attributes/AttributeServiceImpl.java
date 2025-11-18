package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.commons.dao.JdbcFunctionDao;
import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.commons.utils.QueryUtils;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.kpi.attributes.dto.AttributeDto;
import com.datasoft.luncheon.kpi.hierarchy.HierarchyDao;
import com.datasoft.luncheon.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AttributeServiceImpl implements  AttributeService{

    private final JdbcFunctionDao jdbcFunctionDao;
    private final UserService userService;
    private final AttributeDao attributeDao;


    public AttributeServiceImpl(JdbcFunctionDao jdbcFunctionDao, UserService userService, AttributeDao attributeDao) {
        this.jdbcFunctionDao = jdbcFunctionDao;
        this.userService = userService;
        this.attributeDao = attributeDao;
    }

    public Integer getUserId(String userId) {
        return jdbcFunctionDao.getJdbcTemplate().queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Integer.class,
                userId
        );
    }

    @Override
    public ApiResponse allAttribute(KpiConfigParams params) {
        try {
            Map<String, Object> param = new LinkedHashMap<>();
//            Integer user = getUserId(userId);
            param.put("userId", userService.getCurrentUserId());
            param.put("pid", params.getPid());
            param.put("filterParam", QueryUtils.filterQueryBuilder(params.getFilterParam()));
            param.put("searchParam", params.getSearchParam());
            param.put("orderParam", params.getOrderParam());
            param.put("orderType", params.getOrderType());
            param.put("paramLimit", params.getParamLimit());
            param.put("paramOffset", params.getParamOffset());
            param.put("isFilterValueString", "");
            Map<String, Object> procedureResult = jdbcFunctionDao.getProcedureResult("sp_get_attribute_list", param);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", procedureResult.get("#result-set-1"));
            return new ApiResponse(HttpStatus.OK.value(),"Attribute List Fetched Successfully",result);
        } catch (Exception e) {
            log.error("Error =>{}, Reason =>{}, Stacktrace =>{}", e.getMessage(), e.getCause(), e);
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An Error Found", null);
        }
    }



//    @Override
//    public ResponseEntity<?> saveAttribute(AttributeDto attribute) {
//        try {
//            //productInfo.setCreatedBy(userService.getId());
//            if (Objects.nonNull(attribute.getId())) {
//                attributeDao.save(attribute);
//                return new ResponseEntity<>(new ApiResponse(200, "Attribute configured successfully", null), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new ApiResponse(200, "Attribute re-configured successfully", null), HttpStatus.OK);
//            }
//
//        } catch (DuplicateKeyException e) {
//            log.error("Product Type add/update - duplicate name {}", attribute, e);
//            String massege = (Objects.nonNull(e.getMessage()) && e.getMessage().contains("conf_product_type.UNQ_conf_product_type_pid_irpid_arpid")) ? "\"Mapping information is already mapped to Product Type. Please provide a\n" +
//                    "different mapping information" : "Given name already exists. Please provide another name";
//            return new ResponseEntity(new ApiResponse(409, massege, null), HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e.getCause());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

    @Override
    public ResponseEntity<?> saveAttribute(String selectedKpiType,String attributeName,String userId) {
        attributeDao.save(selectedKpiType,attributeName,userId);
        return new ResponseEntity<>(new ApiResponse(200, "KPI Attribute configured successfully", null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAttribute(String selectedKpiType, String attributeName, String userId,String id) {
        attributeDao.update(selectedKpiType,attributeName,userId,id);
        return new ResponseEntity<>(new ApiResponse(200, "KPI Attribute Reconfigured successfully", null), HttpStatus.OK);
    }


}
