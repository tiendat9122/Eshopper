package com.ecommerce.api.eshopper.service.product_service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.api.eshopper.dto.ProductQuantityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Override
    public List<Product> findAllProductIsActive() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> findAllById(Iterable<Long> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findProductByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    public List<Product> findProductByAuthor(String author) {
        return productRepository.findByAuthorName(author);
    }

    @Override
    public Page<Product> findProductByKeyWord(String name, Pageable page) {
        return productRepository.findByKeyword(name, page);
    }
    
    @Override
    public List<Product> findProductRandom() {
        return productRepository.findRandomProducts();
    }

    @Override
    public List<Product> getAllProductHotActiveTrue() {
        return productRepository.findHotActiveProductsLimitedTo8();
    }

    @Override
    public List<Product> getAllProductByCategoryId(Long categoryId) {
        return productRepository.findByCategoriesId(categoryId);
    }

    @Override
    public List<ProductQuantityDto> getAllProductQuantitySold() {
        return productRepository.findTopProductsByMaxQuantityWithLimit8();
    }

    @Override
    public Page<Product> getAllProductByCategoryAndName(Long categoryId, String productName, Pageable pageable) {
        return productRepository.findByCategoryIdAndProductName(categoryId, productName, pageable);
    }

    @Override
    public Page<Product> getAllProductByAuthorAndName(Long authorId, String productName, Pageable pageable) {
        return productRepository.findByAuthorIdAndProductName(authorId, productName, pageable);
    }

    // Save and update
    @Override
    public List<Product> saveAllProduct(List<Product> products) {
        return productRepository.saveAll(products);
    }

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
