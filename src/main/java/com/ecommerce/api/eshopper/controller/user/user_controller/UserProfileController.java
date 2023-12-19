package com.ecommerce.api.eshopper.controller.user.user_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class UserProfileController {

    @GetMapping("/profile")
    public String userStore(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/profile.html");
        return "user/layouts/app";
    }

}
