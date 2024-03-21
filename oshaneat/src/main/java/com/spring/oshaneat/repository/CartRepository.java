package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.Cart;
import com.spring.oshaneat.entity.Food;
import com.spring.oshaneat.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserAndFood(Users user, Food food);

    List<Cart> findByUser(Users user);

    void deleteByUserAndFood(Users user, Food food);
}
