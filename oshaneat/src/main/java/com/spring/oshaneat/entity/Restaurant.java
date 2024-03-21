package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
@Entity(name = "Restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "isFreeShip")
    private boolean isFreeShip;

    @Column(name = "address")
    private String address;

    @Column(name = "openDate")
    private String openDate;

    @OneToMany(mappedBy = "restaurant")
    private Set<RatingRestaurant> listRatingRestaurants;

    @OneToMany(mappedBy = "restaurant")
    private Set<MenuRestaurant> listMenuRestaurants;

    @OneToMany(mappedBy = "restaurant")
    private Set<Food> listFoods;

    @OneToMany(mappedBy = "restaurant")
    private Set<Orders> listOrders;
}
