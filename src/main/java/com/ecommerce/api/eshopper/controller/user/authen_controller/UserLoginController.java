package com.ecommerce.api.eshopper.controller.user.authen_controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userauth")
public class UserLoginController {
    
    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("eshopperAuthenFragment", "user/component_authen/user_login.html");
        return "user/layout_authen/app";
    }

}
