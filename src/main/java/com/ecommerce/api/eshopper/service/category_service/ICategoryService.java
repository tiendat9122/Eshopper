package com.ecommerce.api.eshopper.service.category_service;

import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    // Get and find
    List<Category> findAllCategory();

    Optional<Category> findCategoryById(Long id);

    // Save and update
    Product saveProduct(Product product);

    Category saveCategory(Category category);

    // Remove
    void deleteCategory(Category category);

}
