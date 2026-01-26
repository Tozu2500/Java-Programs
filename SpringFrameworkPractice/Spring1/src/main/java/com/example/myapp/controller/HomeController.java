package com.example.myapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Spring Boot Product API");
        response.put("version", "1.0.0");
        response.put("description", "A simple REST API for managing products");
        response.put("endpoints", new HashMap<String, String>() {{
            put("GET /api/products", "Get all products");
            put("GET /api/products/{id}", "Get product by ID");
            put("POST /api/products", "Create new product");
            put("PUT /api/products/{id}", "Update product");
            put("DELETE /api/products/{id}", "Delete product");
            put("GET /api/products/search?name=value", "Search by name");
            put("GET /api/products/search?minPrice=10&maxPrice=100", "Search by price range");
        }});
        response.put("sample_product", new HashMap<String, Object>() {{
            put("name", "Sample Product");
            put("price", 29.99);
            put("description", "This is a sample product");
        }});
        return response;
    }
}
