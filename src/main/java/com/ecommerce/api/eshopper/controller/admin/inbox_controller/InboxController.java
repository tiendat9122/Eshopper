package com.ecommerce.api.eshopper.controller.admin.inbox_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/inbox")
public class InboxController {
    
    @GetMapping
    public String profileInfoPage(Model model) {
        model.addAttribute("contentFragment", "admin/inboxes/list.html");
        model.addAttribute("modalEdit", "admin/inboxes/edit.html");
        model.addAttribute("modalCreate", "admin/inboxes/create.html");
        model.addAttribute("modalDelete", "admin/inboxes/delete.html");
        return "admin/layouts/app";
    }

}
