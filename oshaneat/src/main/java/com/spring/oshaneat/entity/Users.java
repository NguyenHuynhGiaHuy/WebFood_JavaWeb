package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter @Setter
@Entity(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "pass")
    private String pass;

    @Column(name = "fullname")
    private String fullName;

    @Column
    private String phone;

    @Column
    private String address;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Roles roles;

    @OneToMany(mappedBy = "user")
    private Set<Orders> listOrders;

    @OneToMany(mappedBy = "user")
    private Set<RatingRestaurant> listRatingRestaurants;

    @OneToMany(mappedBy = "user")
    private Set<Cart> listCart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordResetToken passwordResetToken;
}
