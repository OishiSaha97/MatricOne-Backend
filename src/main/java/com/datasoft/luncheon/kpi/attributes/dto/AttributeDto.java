package com.datasoft.luncheon.kpi.attributes.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttributeDto {
    private Integer id;
    private String name;
    private String description;
    private Integer createdBy;
    private Date createdAt;
    private boolean changed;
    private String combinedString;
}
