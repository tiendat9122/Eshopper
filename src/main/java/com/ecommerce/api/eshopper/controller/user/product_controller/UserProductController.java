package com.ecommerce.api.eshopper.controller.user.product_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserProductController {

    @GetMapping("/shop")
    public String userShop(Model model) {
        model.addAttribute("page", 0);
        model.addAttribute("size", 0);
        return "user/shop";
    }
}
