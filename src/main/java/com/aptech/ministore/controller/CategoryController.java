package com.aptech.ministore.controller;

import com.aptech.ministore.entity.Categories;
import com.aptech.ministore.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/")
    public List<Categories> get(){
        return categoryService.get();
    }

    @PostMapping
    public Categories add(@RequestBody Categories categories) throws Exception {
        return categoryService.add(categories);
    }

    @GetMapping
    public Optional<Categories> getById(@RequestParam int id){
        return categoryService.getById(id);
    }

    @DeleteMapping
    public void deleteCate(@RequestParam int id){
        categoryService.delete(id);
    }
}

