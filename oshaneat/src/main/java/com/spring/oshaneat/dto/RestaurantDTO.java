package com.spring.oshaneat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter @Setter
public class RestaurantDTO {
    private int id;
    private String title;
    private String subtitle;
    private String description;
    private String image;
    private double rating;
    private int countCustomers;
    private boolean isFreeship;
    private String openDate;
    private String address;
    private List<CategoryDTO> categories;
}
