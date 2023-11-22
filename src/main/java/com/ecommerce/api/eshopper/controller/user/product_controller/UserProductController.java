package com.ecommerce.api.eshopper.controller.user.product_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class UserProductController {

    @GetMapping("/shop")
    public String userShop(Model model) {
        model.addAttribute("page", 0);
        model.addAttribute("size", 0);
        return "user/shop";
    }

    @GetMapping("/product/detail")
    public String userProductDetail(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/detail.html");
        return "user/layouts/app";
    }

}
