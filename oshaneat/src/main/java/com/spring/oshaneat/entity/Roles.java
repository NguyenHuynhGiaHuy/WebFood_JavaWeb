package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter @Setter
@Entity(name = "Roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "roleName")
    private String roleName;

    @OneToMany(mappedBy = "roles")
    private Set<Users> users;
}
