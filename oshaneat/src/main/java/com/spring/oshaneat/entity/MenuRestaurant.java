package com.spring.oshaneat.entity;

import com.spring.oshaneat.entity.keys.KeyMenuRestaurant;
import com.spring.oshaneat.entity.keys.KeyOrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Entity(name = "MenuRestaurant")
public class MenuRestaurant {
    @EmbeddedId
    private KeyMenuRestaurant keys;

    @ManyToOne
    @JoinColumn(name = "cateId", insertable = false, updatable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "resId", insertable = false, updatable = false)
    private Restaurant restaurant;
}
