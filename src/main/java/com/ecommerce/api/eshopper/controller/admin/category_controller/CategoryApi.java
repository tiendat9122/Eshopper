package com.ecommerce.api.eshopper.controller.admin.category_controller;

import com.ecommerce.api.eshopper.dto.CategoryDto;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryApi {

    private final ICategoryService categoryService;

    private final IProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategory(@RequestParam(name = "id", required = false) Long id) {
        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                return new ResponseEntity<>(category, HttpStatus.OK);
            } else {
                List<Category> categories = categoryService.findAllCategory();
                return new ResponseEntity<>(categories, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertCategory(@RequestBody CategoryDto categoryDto) {

        try {
            Category category = new Category();
            category.setName(categoryDto.getName());

            /*List<Long> productIds = categoryDto.getProductIds();
            Set<Product> setProducts = new HashSet<>();
            for(Long productId : productIds) {
                Product product = productService.findProductById(productId).orElseThrow();
                setProducts.add(product);
            }
            category.setProducts(setProducts);*/

            Category categoryInserted = categoryService.saveCategory(category);

            /*var products = productService.findAllById(categoryDto.getProductIds());
            products.forEach(product -> product.getCategories().add(categoryInserted));
            productService.saveAllProduct(products);*/

            return new ResponseEntity<>(categoryInserted, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateCategory(@RequestParam(name = "id") Long id, @RequestBody CategoryDto categoryDto) {
        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                category.setName(categoryDto.getName());

                /*var productsFromDB = category.getProducts();

                List<Long> productIds = categoryDto.getProductIds();

                List<Product> productsWillRemove = new ArrayList<>();
                for(Product product : productsFromDB) {
                    if(!productIds.contains(product.getId())) {
                        productsWillRemove.add(product);
                    }
                }

                productsFromDB.removeAll(productsWillRemove);

                for(Product product: productsWillRemove) {
                    var categories = product.getCategories();
                    categories.remove(category);
                }

                var productIdFromDB = new ArrayList<Long>();
                for(var product: productsFromDB) {
                    productIdFromDB.add(product.getId());
                }

                var productIdsWillAdd = new ArrayList<Long>();
                for(var idFromDTO : productIds) {
                    if (!productIdFromDB.contains(idFromDTO))
                        productIdsWillAdd.add(idFromDTO);
                }

                var prodcutWillAdd = productService.findAllById(productIdsWillAdd);

                productsFromDB.addAll(prodcutWillAdd);

                for(var product: prodcutWillAdd) {
                    product.getCategories().add(category);
                }*/

                Category categoryUpdated = categoryService.saveCategory(category);

                return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what role you want to for update", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam(name = "id") Long id) {

        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                categoryService.deleteCategory(category);
                return new ResponseEntity<>("Deleted category successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what category you want to for delete?",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
