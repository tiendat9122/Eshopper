package com.ecommerce.api.eshopper.controller.admin.login_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin/login")
public class LoginController {

    //Login controller
    @GetMapping
    public String page() {
        return "login";
    }
}
