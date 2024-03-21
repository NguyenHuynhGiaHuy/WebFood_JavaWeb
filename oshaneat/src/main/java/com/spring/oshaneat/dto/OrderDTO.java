package com.spring.oshaneat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private int orderId;
    private String restaurant;
    private String address;
    private List<String> categoryList;
    private String date;
    private List<FoodDTO> foodDTOList;
    private Long total;
}
