package com.spring.oshaneat.repository;

import com.spring.oshaneat.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByTitleContainingIgnoreCase(String keyword);

    @Query("SELECT r FROM Restaurant r WHERE " +
            "CONCAT(r.title, ' ', r.subtitle, ' ', r.description, ' ', r.address, ' ', r.openDate) LIKE %?1%")
    Page<Restaurant> findBySearchValue(String searchValue, Pageable pageable);

}
