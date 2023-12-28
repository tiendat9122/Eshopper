package com.ecommerce.api.eshopper.controller.admin.profile_controller;

import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileApi {

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    private final IUserService userService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/get")
    public ResponseEntity<?> getProfile(@RequestParam(name = "id") Long id) {

        try {
            if (id != null) {
                User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestParam(name = "id") Long id, @ModelAttribute UserDto userDto) {

        try {
            if(id != null) {
                User user = userService.findUserById(id).orElseThrow();
                user.setFull_name(userDto.getFull_name());
                user.setUser_name(userDto.getUser_name());
                user.setEmail(userDto.getEmail());
                user.setAddress(userDto.getAddress());
                user.setPhone_number(userDto.getPhone_number());
                user.setBirth_day(userDto.getBirth_day());

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
                User profileUpdated = userService.saveUser(user);
                return new ResponseEntity<>(profileUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestParam(name = "id") Long id, @RequestBody UserDto userDto) {

        try {
            if(id != null) {
                User user = userService.findUserById(id).orElseThrow();

                String password_old = user.getPassword();
                String confirm_password_old = userDto.getPassword();
                
                if(passwordEncoder.matches(confirm_password_old, password_old)) {
                    user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                User profileUpdated = userService.saveUser(user);
                return new ResponseEntity<>(profileUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
