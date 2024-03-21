package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "is_free_ship")
    private boolean isFreeShip;

    @Column(name = "price")
    private Long price;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "cateId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "resId")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "food")
    private Set<OrderItem> listOrderItems;

    @OneToMany(mappedBy = "food")
    private Set<Cart> listCart;
}
