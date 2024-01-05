package com.ecommerce.api.eshopper.controller.user.category_controller;

import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.service.category_service.CategoryService;
import com.ecommerce.api.eshopper.service.product_service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/category")
public class UserCategoryApi {

    private final ProductService productService;

    private final CategoryService categoryService;

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

}
