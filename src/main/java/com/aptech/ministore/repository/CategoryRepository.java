package com.aptech.ministore.repository;

import com.aptech.ministore.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    Optional<Categories> findByName(String name);
}