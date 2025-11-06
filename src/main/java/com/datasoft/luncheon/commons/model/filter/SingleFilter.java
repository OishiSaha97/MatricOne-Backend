package com.datasoft.luncheon.commons.model.filter;

import lombok.Data;

@Data
public
class SingleFilter{
    private String prefix;
    private String key;
    private String compare;
    private String value;
    private String value2;
    private String dataType;
}
