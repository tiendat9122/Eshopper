package com.ecommerce.api.eshopper.controller.user.contact_controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eshopper")
public class UserContactController {

    @GetMapping("/contact")
    public String userContact(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/contact.html");
        return "user/layouts/app";
    }
    
}
