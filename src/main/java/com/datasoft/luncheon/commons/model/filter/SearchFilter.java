package com.datasoft.luncheon.commons.model.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchFilter {

    private String param;
    private String searchParam;
    private String sortBy;
    private String sortOrder;
    private Integer limit;
    private Integer offset;
    private List<SingleFilter> filters;

}
