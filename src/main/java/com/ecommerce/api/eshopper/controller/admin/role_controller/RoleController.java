package com.ecommerce.api.eshopper.controller.admin.role_controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/role")
public class RoleController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/roles/index.html");
        model.addAttribute("modalCreate", "admin/roles/create.html");
        model.addAttribute("modalEdit", "admin/roles/edit.html");
        model.addAttribute("modalDelete", "admin/roles/delete.html");
        return "admin/layouts/app";
    }
}
