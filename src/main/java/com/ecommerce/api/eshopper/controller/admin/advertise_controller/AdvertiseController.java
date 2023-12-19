package com.ecommerce.api.eshopper.controller.admin.advertise_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/advertise")
public class AdvertiseController {

    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/advertises/index.html");
        model.addAttribute("modalCreate", "admin/advertises/create.html");
        model.addAttribute("modalEdit", "admin/advertises/edit.html");
        model.addAttribute("modalDelete", "admin/advertises/delete.html");
        return "admin/layouts/app";
    }

}
