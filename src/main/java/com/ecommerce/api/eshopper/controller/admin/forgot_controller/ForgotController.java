package com.ecommerce.api.eshopper.controller.admin.forgot_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/listforgot")
public class ForgotController {

    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/forgots/index.html");
        model.addAttribute("modalCreate", "admin/forgots/create.html");
        model.addAttribute("modalEdit", "admin/forgots/edit.html");
        model.addAttribute("modalDelete", "admin/forgots/delete.html");
        return "admin/layouts/app";
    }

}
