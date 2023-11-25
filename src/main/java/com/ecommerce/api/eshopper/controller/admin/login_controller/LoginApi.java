package com.ecommerce.api.eshopper.controller.admin.login_controller;

import com.ecommerce.api.eshopper.auth.AuthenticationRequest;
import com.ecommerce.api.eshopper.dto.ForgetDto;
import com.ecommerce.api.eshopper.dto.RegisterDto;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.authentication_service.AuthenticationService;
import com.ecommerce.api.eshopper.service.role_service.IRoleService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginApi {

    private final AuthenticationService authenticationService;

    private final IUserService userService;

    private final IRoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginAuth(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        try {
            User user = new User();
            user.setFull_name(registerDto.getFull_name());
            user.setUser_name(registerDto.getUser_name());
            user.setEmail(registerDto.getEmail());
            user.setActive(true);
            user.setAddress(registerDto.getAddress());
            user.setBirth_day(registerDto.getBirth_day());
            user.setPhone_number(registerDto.getPhone_number());
            
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            Long id = 1L;
            Set<Role> roles = new HashSet<>();
            Role role = roleService.findRoleById(id).orElseThrow();
            roles.add(role);
            user.setRole(roles);

            User userRegistered = userService.saveUser(user);

            return new ResponseEntity<>(userRegistered, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/forget")
    public ResponseEntity<?> forget(@RequestBody ForgetDto forgetDto) {

        try {
            User user = userService.findUserByEmail(forgetDto.getEmail());
            if(user == null) {
                return new ResponseEntity<>("Account doesn't exist", HttpStatus.NOT_FOUND);
            } else {
                user.setEmail(forgetDto.getEmail());
                user.setPassword(passwordEncoder.encode(forgetDto.getNew_password()));
                User userUpdated = userService.saveUser(user);
                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

}