package com.aptech.ministore.repository;

import com.aptech.ministore.entity.Categories;
import com.aptech.ministore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategories (Categories categories);
}

