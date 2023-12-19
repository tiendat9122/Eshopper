package com.ecommerce.api.eshopper.controller.user.home_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class HomeController {

    @GetMapping("/home")
    public String userStore(Model model) {
        model.addAttribute("page", 0);
        model.addAttribute("size", 0);
        model.addAttribute("eshopperContentFragment", "user/components/home.html");
        return "user/layouts/app";
    }

}
