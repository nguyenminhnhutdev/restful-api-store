package com.aptech.ministore.service.Impl;

import com.aptech.ministore.entity.Product;
import com.aptech.ministore.repository.ProductRepository;
import com.aptech.ministore.service.ICategoryService;
import com.aptech.ministore.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final ICategoryService ICategoryService;
    @Override
    public List<Product> get() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getByCate(int idCate) {
        return productRepository.findByCategories(ICategoryService.getById(idCate).get());
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public void hiddenProduct(int id) {

    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }
}

