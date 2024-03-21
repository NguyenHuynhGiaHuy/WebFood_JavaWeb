package com.spring.oshaneat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String token;

    @Column
    private LocalDateTime expiryDateTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
