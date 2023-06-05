package com.aptech.ministore.service;

import com.aptech.ministore.entity.Categories;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    //get all
    List<Categories> get();
    //add cate
    Categories add(Categories categories) throws Exception;
    Optional<Categories> add1(Categories categories);
    //update
    Categories update();
    //delete
    void delete(int id);

    Optional<Categories> getById(int id);
}
