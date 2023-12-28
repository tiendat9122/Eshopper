package com.ecommerce.api.eshopper.controller.user.category_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/eshopper")
public class UserCategoryController {

    @GetMapping("/category")
    public String userCategory(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/category.html");
        return "user/layouts/app";
    }

}
