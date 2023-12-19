package com.ecommerce.api.eshopper.controller.user.authen_controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userauth")
public class UserLoginController {
    
    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("eshopperAuthenFragment", "user/component_authen/user_login.html");
        return "user/layout_authen/app";
    }

}
