package com.example.myapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring data JPA automatic implementations

    // Custom query methods
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.price > :price ORDER BY p.price DESC")
    List<Product> findExpensiveProducts(@Param("price") Double price);

    @Query(value = "SELECT * FROM products WHERE created_at > NOW() - INTERVAL 7 DAY",
        nativeQuery = true)
    List<Product> findRecentProducts();
}