package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity(name = "Category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nameCate")
    private String nameCate;

    @OneToMany(mappedBy = "category")
    private Set<Food> listFoods;

    @OneToMany(mappedBy = "category")
    private Set<MenuRestaurant> listMenuRestaurants;
}
