package com.ecommerce.api.eshopper.controller.admin.product_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/products/index.html");
        model.addAttribute("modalCreate", "admin/products/create.html");
        model.addAttribute("modalEdit", "admin/products/edit.html");
        model.addAttribute("modalDelete", "admin/products/delete.html");
        return "admin/layouts/app";
    }
}
