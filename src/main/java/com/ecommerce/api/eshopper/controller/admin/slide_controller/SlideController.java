package com.ecommerce.api.eshopper.controller.admin.slide_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/slide")
public class SlideController {

    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/slides/index.html");
        model.addAttribute("modalCreate", "admin/slides/create.html");
        model.addAttribute("modalEdit", "admin/slides/edit.html");
        model.addAttribute("modalDelete", "admin/slides/delete.html");
        return "admin/layouts/app";
    }

}
