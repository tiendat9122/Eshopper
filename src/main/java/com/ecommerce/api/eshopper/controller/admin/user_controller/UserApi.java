package com.ecommerce.api.eshopper.controller.admin.user_controller;

import com.ecommerce.api.eshopper.dto.UserActiveDto;
import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import com.ecommerce.api.eshopper.service.role_service.IRoleService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    Logger logger = LoggerFactory.getLogger(UserApi.class);

    private final IUserService userService;

    private final IRoleService roleService;

    private final IOrdersService ordersService;

    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getUser(@RequestParam(name = "id", required = false) Long id) {

        try {
            if (id != null) {
                User user = userService.findUserById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id = " + id));
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                List<User> users = userService.findAllUser();
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/download/{avatar}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getAvatar(@PathVariable(name = "avatar") String avatar) {

        if (!avatar.equals("") || avatar != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "users", avatar);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertUser(@ModelAttribute UserDto userDto) {

        try {
            User userExisted = userService.findUserByEmail(userDto.getEmail());
            if (userExisted != null) {
                return new ResponseEntity<>("Email have exists already", HttpStatus.BAD_REQUEST);
            } else {
                User user = new User();
                user.setFull_name(userDto.getFull_name());
                user.setUser_name(userDto.getUser_name());
                user.setEmail(userDto.getEmail());
                user.setAddress(userDto.getAddress());
                user.setPhone_number(userDto.getPhone_number());
                user.setBirth_day(userDto.getBirth_day());
                user.setActive(userDto.isActive());

                // set encrypassword
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                // add avatar for user
                if (userDto.getAvatar_file() == null || userDto.getAvatar_file().isEmpty()) {
                    user.setAvatar(null);
                } else {
                    Path path = Paths.get(FILE_DIRECTORY, "users");

                    try {
                        InputStream inputStream = userDto.getAvatar_file().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = userDto.getAvatar_file().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if (lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                        String fileSave = userDto.getUser_name() + "_" + userDto.getAvatar_file().getName() + timeStamp
                                + extension;

                        Files.copy(inputStream, path.resolve(fileSave),
                                StandardCopyOption.REPLACE_EXISTING);

                        user.setAvatar(fileSave);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // set roles for user
                List<Long> roleIds = userDto.getRoleIds();
                Set<Role> roles = new HashSet<>();
                if (roleIds == null) {
                    roles = new HashSet<>();
                } else {
                    for (Long roleId : roleIds) {
                        Role role = roleService.findRoleById(roleId).orElseThrow();
                        roles.add(role);
                    }
                }
                user.setRole(roles);

                User userInserted = userService.saveUser(user);
                return new ResponseEntity<>(userInserted, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam(name = "id") Long id, @ModelAttribute UserDto userDto) {

        try {
            if (id != null) {
                User user = userService.findUserById(id).orElseThrow();
                user.setFull_name(userDto.getFull_name());
                user.setUser_name(userDto.getUser_name());
                user.setEmail(userDto.getEmail());
                user.setAddress(userDto.getAddress());
                user.setPhone_number(userDto.getPhone_number());
                user.setBirth_day(userDto.getBirth_day());
                user.setActive(userDto.isActive());

                // update avatar for user
                File fileOld = new File(FILE_DIRECTORY + "/users/" + user.getAvatar());
                if (userDto.getAvatar_file() != null && !userDto.getAvatar_file().isEmpty()) {
                    try {
                        Path path = Paths.get(FILE_DIRECTORY, "users");
                        InputStream inputStream = userDto.getAvatar_file().getInputStream();
                        long timeStamp = new Date().getTime();
                        String fileName = userDto.getAvatar_file().getOriginalFilename();
                        int lastDotIndex = fileName.lastIndexOf('.');
                        String extension;
                        if (lastDotIndex > 0) {
                            extension = fileName.substring(lastDotIndex);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }

                        String fileSave = userDto.getUser_name() + "_" + userDto.getAvatar_file().getName() + timeStamp
                                + extension;

                        if (fileOld.exists()) {
                            fileOld.delete();
                            Files.copy(inputStream, path.resolve(fileSave),
                                    StandardCopyOption.REPLACE_EXISTING);
                            user.setAvatar(fileSave);
                        } else {
                            Files.copy(inputStream, path.resolve(fileSave),
                                    StandardCopyOption.REPLACE_EXISTING);
                            user.setAvatar(fileSave);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (userDto.getAvatar().isBlank()) {
                    if (fileOld.exists()) {
                        fileOld.delete();
                    }
                    user.setAvatar(null);
                }
                
                // update roles for user
                List<Long> roleIds = userDto.getRoleIds();
                Set<Role> roles = new HashSet<>();
                if (roleIds == null) {
                    roles = new HashSet<>();
                } else {
                    for (Long roleId : roleIds) {
                        Role role = roleService.findRoleById(roleId).orElseThrow();
                        roles.add(role);
                    }
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

    @PutMapping("/active")
    public ResponseEntity<?> activeUser(@RequestBody UserActiveDto userActiveDto) {

        try {
            Long userId = userActiveDto.getId();
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));

            user.setActive(userActiveDto.isActive());
            userService.saveUser(user);
            return new ResponseEntity<>("Changed state active of user", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "id") Long id) {

        try {
            if (id != null) {
                User user = userService.findUserById(id).orElseThrow();

                // delete user's avatar image on server
                try {
                    String avatar = user.getAvatar();
                    Path filePath = Paths.get(FILE_DIRECTORY, "users", avatar);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                } catch (Exception e) {
                    logger.info("Cannot read file image because it's haven't been exists");
                }

                // delete user's orders
                Set<Orders> orders = user.getOrders();
                for (Orders order : orders) {
                    order.setUser(null);
                }

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
