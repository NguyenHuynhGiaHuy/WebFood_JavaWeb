package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryReposity extends JpaRepository<Category, Integer> {
}
