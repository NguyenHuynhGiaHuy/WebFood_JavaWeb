package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByTitleContainingIgnoreCase(String keyword);
}
