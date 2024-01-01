package com.ecommerce.api.eshopper.service.product_service;

import com.ecommerce.api.eshopper.dto.ProductQuantityDto;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
public interface IProductService {

    // Get and find
    List<Product> findAllProduct();

    List<Product> findAllProductIsActive();

    Optional<Product> findProductById(Long id);

    Optional<Product> findProductByName(String name);

    List<Product> findProductByNameContaining(String name);

    List<Product> findProductByAuthor(String name);

    Page<Product> findProductByKeyWord(String name, Pageable page);

    List<Product> findProductRandom();

    List<Product> getAllProductHotActiveTrue();

    List<Product> getAllProductByCategoryId(Long categoryId);

    List<ProductQuantityDto> getAllProductQuantitySold();

    // Save and update
    List<Product> saveAllProduct(List<Product> products);

    Product saveProduct(Product product);

    Category saveCategory(Category category);

    // Remove
    void deleteProduct(Product product);

    void deleteProductById(Long id);

    List<Product> findAllById(Iterable<Long> id);

}
