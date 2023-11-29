package com.ecommerce.api.eshopper.controller.admin.orders_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class OrdersController {
    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/orders/index.html");
        model.addAttribute("modalCreate", "admin/orders/create.html");
        model.addAttribute("modalEdit", "admin/orders/edit.html");
        model.addAttribute("modalDelete", "admin/orders/delete.html");
        return "admin/layouts/app";
    }
}
