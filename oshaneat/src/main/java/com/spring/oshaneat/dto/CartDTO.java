package com.spring.oshaneat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartDTO {
    List<FoodDTO> foodDTOList;
    private double totalPrice;
}
