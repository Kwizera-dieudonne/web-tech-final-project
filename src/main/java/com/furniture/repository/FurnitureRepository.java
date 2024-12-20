package com.furniture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.furniture.domain.Category;
import com.furniture.domain.Furniture;

@Repository
public interface FurnitureRepository  extends JpaRepository<Furniture, Long> {
    List<Furniture> findByCategory(Category category);
    List<Furniture> findByPriceLessThan(Double price);
    List<Furniture> findByNameContainingIgnoreCase(String name);
    @Query("SELECT f FROM Furniture f " +
    "WHERE LOWER(f.name) LIKE CONCAT('%', :query, '%') " +
    "OR LOWER(f.category.name) LIKE CONCAT('%', :query, '%')")
    List<Furniture> globalSearch(@Param("query") String query);
}