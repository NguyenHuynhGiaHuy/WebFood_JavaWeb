package com.spring.oshaneat.entity;

import com.spring.oshaneat.entity.keys.KeyOrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Entity(name = "OrderItem")
public class OrderItem {

    @EmbeddedId
    private KeyOrderItem keys;

    @ManyToOne
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "foodId", insertable = false, updatable = false)
    private Food food;

    @Column
    private int quantity;

    @Column
    private Long price;
}
