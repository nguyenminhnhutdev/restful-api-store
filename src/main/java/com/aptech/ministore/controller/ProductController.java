package com.aptech.ministore.controller;

import com.aptech.ministore.entity.Product;
import com.aptech.ministore.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping()
    public List<Product> get(){
        return productService.get();
    }

    @GetMapping("/")
    public Optional<Product> getById(@RequestParam int id){
        return productService.getById(id);
    }

//    @PostMapping
//    public Product add(@RequestBody Product product){
//        return IProductService.add(product);
//    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid Product product) {
        return new ResponseEntity<>(productService.add(product), CREATED);
    }
    @GetMapping("/getByCate")
    public List<Product> getByCate(@RequestParam int id){
        return productService.getByCate(id);
    }
}