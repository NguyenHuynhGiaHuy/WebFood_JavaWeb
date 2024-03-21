package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.Cart;
import com.spring.oshaneat.entity.Orders;
import com.spring.oshaneat.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser(Users user);
    Optional<Orders> findByIdAndUser(int id, Users user);
}
