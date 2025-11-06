package com.datasoft.luncheon.weekend;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "conf_weekend")
public class Weekend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer orgId;
    private Integer weekday;
    private Integer createdBy;
    private Integer updatedBy;

}
