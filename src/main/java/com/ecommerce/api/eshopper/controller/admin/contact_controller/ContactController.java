package com.ecommerce.api.eshopper.controller.admin.contact_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/contact")
public class ContactController {

    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/contacts/index.html");
        model.addAttribute("modalCreate", "admin/contacts/create.html");
        model.addAttribute("modalEdit", "admin/contacts/edit.html");
        model.addAttribute("modalDelete", "admin/contacts/delete.html");
        return "admin/layouts/app";
    }

}
