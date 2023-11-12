package com.ecommerce.api.eshopper.controller.admin.user_controller;

import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.role_service.RoleService;
import com.ecommerce.api.eshopper.service.user_service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    private final RoleService roleService;

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(name = "id", required = false) Long id) {
        try {
            if(id != null) {
                User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user by id = " + id));
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                List<User> users = userService.findAllUser();
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertUser(@RequestBody UserDto userDto) {
        try {
            User user = new User();
            user.setFull_name(userDto.getFull_name());
            user.setUser_name(userDto.getUser_name());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());

            List<Long> roleIds = userDto.getRoleIds();
            Set<Role> roles = new HashSet<>();

            if(roleIds == null) {
                roles = new HashSet<>();
            } else {
                for(Long roleId : roleIds) {
                    Role role = roleService.findRoleById(roleId).orElseThrow();
                    roles.add(role);
                }
            }

            user.setRole(roles);
            User userInserted = userService.saveUser(user);
            return new ResponseEntity<>(userInserted, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam(name = "id", required = false) Long id,
                                        @RequestBody UserDto userDto) {
        try {
            if(id != null) {
                User user = userService.findUserById(id).orElseThrow();
                user.setFull_name(userDto.getFull_name());
                user.setUser_name(userDto.getUser_name());
                user.setEmail(userDto.getEmail());
                user.setPassword(userDto.getPassword());

                List<Long> roleIds = userDto.getRoleIds();
                Set<Role> roles = new HashSet<>();
                for(Long roleId : roleIds) {
                    Role role = roleService.findRoleById(roleId).orElseThrow();
                    roles.add(role);
                }
                user.setRole(roles);

                User userUpdated = userService.saveUser(user);

                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "id", required = false) Long id) {
        try {
            if(id != null) {
                User user = userService.findUserById(id).orElseThrow();
                userService.deleteUser(user);
                return new ResponseEntity<>("Deleted user successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
