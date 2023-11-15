package com.ecommerce.api.eshopper.controller.admin.category_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/categories/index.html");
        return "admin/layouts/app";
    }
}
