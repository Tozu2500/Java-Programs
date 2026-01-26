package com.example.myapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.myapp.model.Category;
import com.example.myapp.model.Product;
import com.example.myapp.repository.CategoryRepository;
import com.example.myapp.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(ProductRepository productRepository, 
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create categories
        Category electronics = new Category("Electronics");
        Category clothing = new Category("Clothing");
        Category books = new Category("Books");
        
        categoryRepository.save(electronics);
        categoryRepository.save(clothing);
        categoryRepository.save(books);

        // Create products
        Product laptop = new Product("Laptop", 999.99);
        laptop.setCategory(electronics);
        laptop.setDescription("High-performance laptop for professionals");
        productRepository.save(laptop);

        Product shirt = new Product("T-Shirt", 29.99);
        shirt.setCategory(clothing);
        shirt.setDescription("Comfortable cotton t-shirt");
        productRepository.save(shirt);

        Product book = new Product("Java Programming", 59.99);
        book.setCategory(books);
        book.setDescription("Learn Java from basics to advanced concepts");
        productRepository.save(book);

        System.out.println("Sample data initialized successfully!");
    }
}
