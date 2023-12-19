package com.ecommerce.api.eshopper.controller.admin.login_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class LoginController {

    //Login controller
    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute( "adminAuthenFragment", "admin/component_authen/login.html");
        return "admin/layout_authen/app";
    }

    @GetMapping("/forgot")
    public String adminForgot(Model model) {
        model.addAttribute( "adminAuthenFragment", "admin/component_authen/forgot.html");
        return "admin/layout_authen/app";
    }

}
