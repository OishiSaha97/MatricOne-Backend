package com.datasoft.luncheon.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse {
    private List<Map<String, Object>> content;
    private Integer totalData;
}
