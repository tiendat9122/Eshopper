package com.ecommerce.api.eshopper.controller.admin.role_controller;

import com.ecommerce.api.eshopper.dto.RoleDto;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.role_service.IRoleService;
import com.ecommerce.api.eshopper.service.role_service.RoleService;
import com.ecommerce.api.eshopper.service.user_service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleApi {

    private final RoleService roleService;

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getRole(@RequestParam(name = "id", required = false) Long id) {
        try {
            if(id != null) {
                Role role = roleService.findRoleById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find role with id = " + id));
                return new ResponseEntity<>(role, HttpStatus.OK);
            } else {
                List<Role> roles = roleService.findAllRole();
                return new ResponseEntity<>(roles, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertRole(@RequestBody RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());

        List<Long> userIds = roleDto.getUserIds();
        Set<User> users = new HashSet<>();

        if(userIds == null) {
            users = null;
        } else {
            for(Long userId : userIds) {
                User user = userService.findUserById(userId).orElseThrow();
                user.getRole().add(role);
                users.add(user);
            }
        }

        role.setUser(users);
        Role roleInserted = roleService.saveRole(role);

        return new ResponseEntity<>(roleInserted, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRole(@RequestParam(name = "id", required = false) Long id,
                                        @RequestBody RoleDto roleDto) {
        try {
            if(id != null) {
                Role role = roleService.findRoleById(id).orElseThrow(()->new EntityNotFoundException("Cannot find role with id = " + id));
                role.setName(roleDto.getName());

                List<Long> userIds = roleDto.getUserIds();
                Set<User> users = new HashSet<>();
                if(userIds == null) {
                    users = null;
                } else {
                    for(Long userId : userIds) {
                        User user = userService.findUserById(userId).orElseThrow(()->new EntityNotFoundException("Cannot find user add into role with user_id = " + userId));
                        users.add(user);
                    }
                }

                role.setUser(users);

                Role roleUpdated = roleService.saveRole(role);

                return new ResponseEntity<>(roleUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what role you want to for update", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRole(@RequestParam(name = "id", required = false) Long id) {
        try {
            if(id != null) {
                Role role = roleService.findRoleById(id).orElseThrow(()->new EntityNotFoundException("Cannot find role with id = " + id));
                roleService.deleteRole(role);
                return new ResponseEntity<>("Deleted role successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("I don't know what role you want to for delete?", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
