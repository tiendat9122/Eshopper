package com.ecommerce.api.eshopper.controller.admin.feedback_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

    @GetMapping
    public String page(Model model) {
        model.addAttribute("contentFragment", "admin/feedbacks/index.html");
        model.addAttribute("modalCreate", "admin/feedbacks/create.html");
        model.addAttribute("modalEdit", "admin/feedbacks/edit.html");
        model.addAttribute("modalDelete", "admin/feedbacks/delete.html");
        return "admin/layouts/app";
    }

}
