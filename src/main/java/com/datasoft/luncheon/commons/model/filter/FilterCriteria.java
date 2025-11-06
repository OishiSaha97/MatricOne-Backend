package com.datasoft.luncheon.commons.model.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FilterCriteria {

    private Integer id;
    private Integer traineeTypeId;
    private Integer traineeTypeCategoryId;
    private String traineeTypeCategoryName;
    private Integer trainingTypeId;
    private Integer trainingSourceId;
    private Integer subjectId;
    private Integer topicId;
    private Integer topicName;
    private Integer eventId;
    private String remark;

    private String name;
    private String dhName;
    private String dhMasterAcctNumber;
    private Boolean accountStatus;
    private String description;
    private String designation;
    private String region;
    private String division;
    private String district;
    private String thana;
    private String department;
    private String profileLink;
    private String email;
    private String webSite;
    private String countryName;

    private String capacity;
    private String address;
    private Boolean isTrainingInstitute;
    private String professionalQualification;
    private String educationalQualification;
    private Boolean isTrainer;
    private Boolean isInternal;
    private Boolean status;
    private Boolean isAssigned;
    private String organizationName;
    private String createdAt;

    private Boolean employeeStatus;
    private String employmentType;
    private String employeeType;
    private String employmentStatus;
    private String traineeRegion;
    private String specialDesignation;
    private String regionName;
    private String divisionName;
    private String districtName;
    private String thanaName;
    private String businessRegion;
    private String queryType;
    private String assesseeType;
    private String searchText;
    private String area;
    private String userType;
    private String employeeId;
    private String businessArea;
    private String channelName;
    private String examTitle;
    private String course;
    private String subject;
    private String totalScore;
    private List<SingleFilter> filters;


}
