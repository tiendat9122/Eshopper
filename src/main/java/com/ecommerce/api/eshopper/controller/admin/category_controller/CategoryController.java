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
        model.addAttribute("modalCreate", "admin/categories/create.html");
        model.addAttribute("modalEdit", "admin/categories/edit.html");
        model.addAttribute("modalDelete", "admin/categories/delete.html");
        return "admin/layouts/app";
    }
}
