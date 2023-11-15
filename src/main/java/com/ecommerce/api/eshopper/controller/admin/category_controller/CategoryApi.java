package com.ecommerce.api.eshopper.controller.admin.category_controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.api.eshopper.dto.CategoryDto;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.category_service.CategoryService;
import com.ecommerce.api.eshopper.service.product_service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryApi {

    private final CategoryService categoryService;

    private final ProductService productService;

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
            Category categoryInserted = categoryService.saveCategory(category);
            var products = productService.findAllById(categoryDto.getProductIds());
            products.forEach(product -> product.getCategories().add(categoryInserted));
            productService.saveAll(products);
            return new ResponseEntity<>(categoryInserted, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestParam(name = "id") Long id, @RequestBody CategoryDto categoryDto) {
        try {
            if (id != null) {
                Category category = categoryService.findCategoryById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                category.setName(categoryDto.getName());
                //prodct sẽ bị xóa khỏi categories
                var productDeleteds = new ArrayList<Product>();
                var products = productService.findAllById(categoryDto.getProductIds());
                category.getProducts()
                        .stream()
                        .filter(i -> !products.contains(i))
                        .forEach(i -> {
                            //thêm vào danh sách để cập nhật riêng cho những product bị loại ra
                            productDeleteds.add(i);
                            //xóa category bên prodcut
                            i.getCategories().removeIf(y -> y.getId().equals(category.getId()));
                        });
                
                //xóa product bên category
                category.getProducts().removeIf(i -> !products.contains(i));

                category.getProducts().addAll(products);
                //thêm hai bên
                products.forEach(product -> product.getCategories().add(category));

                productService.saveAll(productDeleteds);
                productService.saveAll(category.getProducts());
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
