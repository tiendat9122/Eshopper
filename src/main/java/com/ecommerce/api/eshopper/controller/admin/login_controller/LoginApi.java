package com.ecommerce.api.eshopper.controller.admin.login_controller;

import com.ecommerce.api.eshopper.auth.AuthenticationRequest;
import com.ecommerce.api.eshopper.dto.RegisterDto;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.authentication_service.AuthenticationService;
import com.ecommerce.api.eshopper.service.role_service.RoleService;
import com.ecommerce.api.eshopper.service.user_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginApi {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAuth(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        User user = new User();
        user.setFull_name(registerDto.getFull_name());
        user.setUser_name(registerDto.getUser_name());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());

        Long id = 1L;
        Role role = roleService.findRoleById(id).orElseThrow();
        user.getRole().add(role);

        User userRegistered = userService.saveUser(user);

        return new ResponseEntity<>(userRegistered, HttpStatus.OK);
    }



}