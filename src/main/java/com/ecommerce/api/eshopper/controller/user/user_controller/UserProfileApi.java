package com.ecommerce.api.eshopper.controller.user.user_controller;

import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.user_service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eshopper")
public class UserProfileApi {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

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

    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(@RequestParam(name = "id") Long id) {

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

    @PutMapping("/profile")
    public ResponseEntity<?> editUserProfile(@RequestParam(name = "id") Long id, @ModelAttribute UserDto userDto) {

        try {
            if (id != null) {
                User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));
                user.setFull_name(userDto.getFull_name());
                user.setUser_name(userDto.getUser_name());
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

                User userUpdated = userService.saveUser(user);
                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Required pass request param", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/password")
    public ResponseEntity<?> passwordUserProfile(@RequestParam(name = "id") Long id, @RequestBody UserDto userDto) {

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
