package com.ecommerce.api.eshopper.service.product_service;

import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.Category;

import java.util.List;
import java.util.Optional;
public interface IProductService {

    // Get and find
    List<Product> findAllProduct();

    Optional<Product> findProductById(Long id);

    // Save and update
    Product saveProduct(Product product);

    Category saveCategory(Category category);

    // Remove
    void deleteProduct(Product product);

    void deleteProductById(Long id);

}
