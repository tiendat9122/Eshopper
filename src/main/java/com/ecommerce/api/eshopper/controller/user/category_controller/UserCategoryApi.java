package com.ecommerce.api.eshopper.controller.user.category_controller;

import com.ecommerce.api.eshopper.service.category_service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/category")
public class UserCategoryApi {

    private final CategoryService categoryService;

}
