package com.aptech.ministore.service;

import com.aptech.ministore.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    //get all
    List<Product> get();
    //getbyid
    Optional<Product> getById(int id);
    //get by category
    List<Product> getByCate(int idCate);
    //update product
    Product update(Product product);
    //hidden product
    void hiddenProduct(int id);


    Product add(Product product);
}
