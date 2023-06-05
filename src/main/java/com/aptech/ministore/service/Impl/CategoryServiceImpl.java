package com.aptech.ministore.service.Impl;

import com.aptech.ministore.entity.Categories;
import com.aptech.ministore.repository.CategoryRepository;
import com.aptech.ministore.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private final CategoryRepository repository;

    @Override
    public List<Categories> get() {
        return repository.findAll();
    }

    @Override
    public Categories add(Categories categories) throws Exception {
        if (checkDuplicate(categories.getName())) {
            return repository.save(categories);
        } else {
            throw new Exception("Duplicate category name");
        }
    }

    @Override
    public Optional<Categories> add1(Categories categories) {
        return repository.findByName(categories.getName());
    }

    @Override
    public Categories update() {
        return null;
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isPresent()) {
            repository.delete(getById(id).get());
        }
    }

    @Override
    public Optional<Categories> getById(int id) {
        return repository.findById(id);
    }

    private boolean checkDuplicate(String name) {
        return repository.findByName(name).isEmpty();
    }
}

