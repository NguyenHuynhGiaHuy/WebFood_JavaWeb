package com.spring.oshaneat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FoodDTO {
    private int id;
    private String title;
    private String image;
    private boolean isFreeShip;
    private String description;
    private Long price;
    private int quantity;
    private String restaurant;
}
