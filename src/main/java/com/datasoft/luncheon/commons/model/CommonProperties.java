package com.datasoft.luncheon.commons.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.util.Date;

@Getter
@Setter
public class CommonProperties {
    // Date fields
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    // Foreign keys
    @CreatedBy
    private Integer createdByUserId;
    @Transient
    private String createdByUserName;
    @LastModifiedBy
    private Integer updatedByUserId;
    @Transient
    private String updatedByUserName;
}
