package com.spring.oshaneat.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FilterRequest {
    private int categoryId;
    private Long minPrice;
    private Long maxPrice;
    private String sortBy;
}
