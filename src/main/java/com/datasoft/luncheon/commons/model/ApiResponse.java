package com.datasoft.luncheon.commons.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private Integer statusCode;
    private String message;
    private Object result;
    private Integer total;

    public ApiResponse(Integer statusCode, String message, Object result) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
    }

}
