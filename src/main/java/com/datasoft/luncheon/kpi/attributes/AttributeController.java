package com.datasoft.luncheon.kpi.attributes;

import com.datasoft.luncheon.commons.model.ApiResponse;
import com.datasoft.luncheon.kpi.KpiConfigParams;
import com.datasoft.luncheon.kpi.attributes.dto.AttributeDto;
import com.datasoft.luncheon.kpi.hierarchy.HierarchyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kpi/attribute")
public class AttributeController {
    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> submitKpiAttribute( @RequestParam(required = false) String selectedKpiType,
                                        @RequestParam(required = false) String attributeName,
                                        @RequestParam(required = false) String userId) {

        return attributeService.saveAttribute(selectedKpiType,attributeName,userId);
    }

//    @PostMapping(value = "/list")
//    public ApiResponse allHierarchy(@RequestBody AttributeDto attribute) {
//        return attributeService.allAttribute(attribute);
//    }

    @PostMapping(value = "/list")
    public ApiResponse allHierarchy(@RequestBody KpiConfigParams params) {
        return attributeService.allAttribute(params);
    }
}
