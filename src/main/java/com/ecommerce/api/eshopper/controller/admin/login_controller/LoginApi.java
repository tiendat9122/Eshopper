package com.ecommerce.api.eshopper.controller.admin.login_controller;

import com.ecommerce.api.eshopper.auth.AuthenticationRequest;
import com.ecommerce.api.eshopper.dto.ForgotDto;
import com.ecommerce.api.eshopper.dto.MailInfo;
import com.ecommerce.api.eshopper.dto.UserDto;
import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.authentication_service.AuthenticationService;
import com.ecommerce.api.eshopper.service.forgot_service.IForgotService;
import com.ecommerce.api.eshopper.service.mailer_service.IMailerService;
import com.ecommerce.api.eshopper.service.role_service.IRoleService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginApi {

    private final AuthenticationService authenticationService;

    private final IUserService userService;

    private final IRoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final IMailerService mailerService;

    private final IForgotService forgotService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAuth(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
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