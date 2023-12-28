package com.ecommerce.api.eshopper.controller.user.authen_controller;

import com.ecommerce.api.eshopper.dto.ForgotDto;
import com.ecommerce.api.eshopper.dto.MailInfo;
import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.forgot_service.IForgotService;
import com.ecommerce.api.eshopper.service.mailer_service.IMailerService;
import com.ecommerce.api.eshopper.service.role_service.IRoleService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userauth")
public class UserLoginApi {

    private final IUserService userService;

    private final IRoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final IMailerService mailerService;

    private final IForgotService forgotService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody UserDto userDto) {

        try {

            User checkUserMail = userService.findUserByEmail(userDto.getEmail());

            if(checkUserMail == null) {
                User user = new User();
                user.setUser_name(userDto.getUser_name());
                user.setFull_name(userDto.getFull_name());
                user.setEmail(userDto.getEmail());
                user.setActive(true);
                user.setAddress(userDto.getAddress());
                user.setPhone_number(userDto.getPhone_number());
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));

                Set<Role> roles = new HashSet<>();
                Role role = roleService.findRoleById(1L).orElseThrow(() -> new EntityNotFoundException("Cannot find role"));
                roles.add(role);
                user.setRole(roles);

                User userRegisted = userService.saveUser(user);
                return new ResponseEntity<>(userRegisted, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email have existed", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotAuth(@RequestBody UserDto userDto) {

        try {

            User user = userService.findUserByEmail(userDto.getEmail());
            if (user != null) {
                //Set code active false for user
                List<Forgot> forgots = forgotService.findForgotByUserAndStatusTrue(user);
                if(!forgots.isEmpty()) {
                    for(Forgot item : forgots) {
                        item.setStatus(false);
                    }
                }

                try {
                    MailInfo mailInfo = new MailInfo();
                    mailInfo.setTo(user.getEmail());

                    StringBuilder txtContent = new StringBuilder();
                    txtContent.append("Verification code: ");
                    Random random = new Random();
                    int code = random.nextInt(900000) + 100000;
                    txtContent.append(code);
                    mailInfo.setBody(txtContent.toString());

                    // Save verification code into database
                    Forgot forgot = new Forgot();
                    String codeString = String.valueOf(code);
                    forgot.setCode(codeString);

                    LocalDateTime now = LocalDateTime.now();
                    forgot.setCodeDate(now);

                    forgot.setStatus(true);
                    forgot.setUser(user);

                    forgotService.saveForgot(forgot);

                    // mailerService.send(mailInfo);
                    mailerService.queue(mailInfo);

                    return new ResponseEntity<>(user, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }

            } else {
                return new ResponseEntity<>("Cannot find any user", HttpStatus.NOT_FOUND);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/verification")
    public ResponseEntity<?> verificationAuth(@RequestBody ForgotDto forgotDto) {

        try {

            User user = userService.findUserById(forgotDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + forgotDto.getUserId()));

            List<Forgot> forgots = forgotService.findForgotByUserAndStatusTrue(user);
            Forgot forgot = forgots.get(0);

            if(forgot.getCode().equals(forgotDto.getCode())) {
                forgot.setStatus(false);
                Forgot forgotUpdated = forgotService.saveForgot(forgot);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Verification code doesn't exactly", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/newpassword")
    public ResponseEntity<?> newPasswordAuth(@RequestBody UserDto userDto) {

        try {

            User user = userService.findUserByEmail(userDto.getEmail());

            if(user != null) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                User userUpdated = userService.saveUser(user);

                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find any user", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
