package com.datasoft.luncheon.guest;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String guestName;
    private String lunch;
    private String userId;
    private Date date;
    @Transient
    private String status;
    private Integer createdBy;
    private Integer updatedBy;


}
