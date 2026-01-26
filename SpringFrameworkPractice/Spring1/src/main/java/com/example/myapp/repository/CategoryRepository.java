package com.example.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myapp.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
