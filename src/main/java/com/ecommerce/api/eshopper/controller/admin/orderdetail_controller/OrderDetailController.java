package com.ecommerce.api.eshopper.controller.admin.orderdetail_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orderdetail")
public class OrderDetailController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/orderdetail/index.html");
        model.addAttribute("modalCreate", "admin/orderdetail/create.html");
        model.addAttribute("modalEdit", "admin/orderdetail/edit.html");
        model.addAttribute("modalDelete", "admin/orderdetail/delete.html");
        return "admin/layouts/app";
    }
}
