package com.spring.oshaneat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CategoryDTO {
    private int categoryId;
    private String name;
    private List<FoodDTO> foodDTOList;
}
