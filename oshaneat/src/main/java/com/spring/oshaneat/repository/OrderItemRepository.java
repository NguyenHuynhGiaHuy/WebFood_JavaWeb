package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.OrderItem;
import com.spring.oshaneat.entity.keys.KeyOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, KeyOrderItem> {
}
