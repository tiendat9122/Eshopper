package com.ecommerce.api.eshopper.controller.user.authen_controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userauth")
public class UserLoginApi {

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

}
