package com.ecommerce.api.eshopper.controller.user.cart_controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class CartController {

    @GetMapping("/cart")
    public String userCart(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/cart.html");
        return "user/layouts/app";
    }

}
