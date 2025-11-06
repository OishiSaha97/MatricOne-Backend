package com.datasoft.luncheon.feedback;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = IDENTITY)

    private Integer id;

    private String createdBy;
    private Integer ratings;
    private String details;

//    private Data created_at;
}
