package com.ecommerce.api.eshopper.service.product_service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.repository.CategoryRepository;
import com.ecommerce.api.eshopper.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    // Get and find
    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> findAllById(Iterable<Long> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    // Save and update
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Iterable<Product> saveAll(Iterable<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Remove
    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
