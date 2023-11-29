package com.ecommerce.api.eshopper.controller.admin.author_controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/author")
public class AuthorController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/authors/index.html");
        model.addAttribute("modalCreate", "admin/authors/create.html");
        model.addAttribute("modalEdit", "admin/authors/edit.html");
        model.addAttribute("modalDelete", "admin/authors/delete.html");
        return "admin/layouts/app";
    }
}
