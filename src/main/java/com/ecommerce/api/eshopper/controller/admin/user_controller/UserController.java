package com.ecommerce.api.eshopper.controller.admin.user_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/users/index.html");
        model.addAttribute("modalCreate", "admin/users/create.html");
        model.addAttribute("modalEdit", "admin/users/edit.html");
        model.addAttribute("modalDelete", "admin/users/delete.html");
        return "admin/layouts/app";
    }
}