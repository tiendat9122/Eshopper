package com.ecommerce.api.eshopper.controller;

import com.ecommerce.api.eshopper.auth.AuthenticationRequest;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.repository.RoleCustomRepo;
import com.ecommerce.api.eshopper.repository.RoleRepository;
import com.ecommerce.api.eshopper.repository.UserRepository;
import com.ecommerce.api.eshopper.service.authentication_service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Scope("prototype")
@RequiredArgsConstructor
@RequestMapping("/greeting")
public class GreetingController {

    private final UserRepository userRepository;

    private final RoleCustomRepo roleCustomRepo;

    private final AuthenticationService authenticationService;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String greetingTemplate = "Hello, %s %s";

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello")
    public ResponseEntity<?> sayHello() {
        return new ResponseEntity<>("Hello everyone", HttpStatus.OK);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserWithRoles(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = new User();
        if(userOptional.isPresent()) {
            user = userOptional.get();
        }
        Set<Role> role = new HashSet<>(userRepository.findRolesByEmail(user.getEmail()));
        Set<Role> set = new HashSet<>();
        // role.stream().forEach(c->set.add(new Role(c.getId(), c.getName(), new HashSet<>())));
        user.getRole().addAll(set);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email);
        Set<Role> role = userRepository.findRolesByEmail(user.getEmail());
        Set<Role> set = new HashSet<>();
        // role.stream().forEach(c->set.add(new Role(c.getId(), c.getName(), new HashSet<>())));
        user.getRole().addAll(set);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/jwt")
    public ResponseEntity<?> getUserJwt(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .id(null)
                .user_name(newUser.getUser_name())
                .full_name(newUser.getFull_name())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .role(roles)
                .build();
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}