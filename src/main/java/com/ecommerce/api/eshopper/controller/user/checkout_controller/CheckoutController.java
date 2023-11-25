package com.ecommerce.api.eshopper.controller.user.checkout_controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class CheckoutController {

    @GetMapping("/checkout")
    public String userCheckout(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/checkout.html");
        return "user/layouts/app";
    }

}
