package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Cart {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Column
    private int quantity;
}
