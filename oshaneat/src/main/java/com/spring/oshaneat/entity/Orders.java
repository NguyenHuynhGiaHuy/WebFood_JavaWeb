package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter @Setter
@Entity(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "resId")
    private Restaurant restaurant;

    @Column(name = "createDate")
    private Date createDate;

    @Column
    private String address;

    @OneToMany(mappedBy = "orders")
    private Set<OrderItem> listOrderItems;
}
