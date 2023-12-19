package com.ecommerce.api.eshopper.controller.admin.profile_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/profile")
public class ProfileController {

    @GetMapping("/get")
    public String profileInfoPage(Model model) {
        model.addAttribute("contentFragment", "admin/profiles/profile.html");
        model.addAttribute("modalEdit", "admin/profiles/edit.html");
        model.addAttribute("modalCreate", "admin/profiles/create.html");
        model.addAttribute("modalDelete", "admin/profiles/delete.html");
        return "admin/layouts/app";
    }

    @GetMapping("/edit")
    public String profileEditPage(Model model) {
        model.addAttribute("contentFragment", "admin/profiles/profile_edit.html");
        model.addAttribute("modalEdit", "admin/profiles/edit.html");
        model.addAttribute("modalCreate", "admin/profiles/create.html");
        model.addAttribute("modalDelete", "admin/profiles/delete.html");
        return "admin/layouts/app";
    }

}