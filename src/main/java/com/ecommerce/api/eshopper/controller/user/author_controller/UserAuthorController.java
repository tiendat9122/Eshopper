package com.ecommerce.api.eshopper.controller.user.author_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/eshopper")
public class UserAuthorController {

    @GetMapping("/author")
    public String userAuthor(Model model) {
        model.addAttribute("eshopperContentFragment", "user/components/author.html");
        return "user/layouts/app";
    }

}
