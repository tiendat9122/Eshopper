package com.ecommerce.api.eshopper.service.category_service;

import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.repository.CategoryRepository;
import com.ecommerce.api.eshopper.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    // Get and find
    @Override
    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> find6Category() {
        return categoryRepository.findTop6ByOrderByIdAsc();
    }

    // Save and update
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Remove
    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

}
