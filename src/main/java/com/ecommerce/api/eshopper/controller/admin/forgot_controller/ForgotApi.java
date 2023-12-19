package com.ecommerce.api.eshopper.controller.admin.forgot_controller;

import com.ecommerce.api.eshopper.dto.FeedbackDto;
import com.ecommerce.api.eshopper.dto.ForgotDto;
import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.forgot_service.ForgotService;
import com.ecommerce.api.eshopper.service.user_service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forgot")
public class ForgotApi {

    private final ForgotService forgotService;

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getForgot(@RequestParam(name = "id", required = false) Long id) {

        try {
            if(id != null) {
                Forgot forgot = forgotService.findForgotById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));
                return new ResponseEntity<>(forgot, HttpStatus.OK);
            } else {
                List<Forgot> forgots = forgotService.getAllForgot();
                return new ResponseEntity<>(forgots, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertForgot(@RequestBody ForgotDto forgotDto) {

        try {
            Forgot forgot = new Forgot();
            forgot.setCode(forgotDto.getCode());
            forgot.setStatus(forgotDto.isStatus());
            forgot.setCodeDate(forgotDto.getCodeDate());

            Long userId = forgotDto.getUserId();
            User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
            forgot.setUser(user);

            Forgot forgotInserted = forgotService.saveForgot(forgot);
            return new ResponseEntity<>(forgotInserted, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateForgot(@RequestParam(name = "id") Long id, @RequestBody ForgotDto forgotDto) {

        try {

            if (id != null) {
                Forgot forgot = forgotService.findForgotById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find forgot with id = " + id));
                forgot.setCode(forgotDto.getCode());
                forgot.setCodeDate(forgotDto.getCodeDate());
                forgot.setStatus(forgotDto.isStatus());

                Long userId = forgotDto.getUserId();
                User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + id));
                forgot.setUser(user);

                Forgot forgotUpdated = forgotService.saveForgot(forgot);

                return new ResponseEntity<>(forgotUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find any forgot", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteForgot(@RequestParam(name = "id") Long id) {

        try {

            if(id != null) {
                Forgot forgot = forgotService.findForgotById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find forgot with id = " + id));
                forgotService.deleteForgot(forgot);
                return new ResponseEntity<>("Deleted forgot successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot find any forgot", HttpStatus.BAD_REQUEST);
            }

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/status")
    public ResponseEntity<?> statusForgot(@RequestBody ForgotDto forgotDto) {

        try {
            Long forgotId = forgotDto.getId();
            Forgot forgot = forgotService.findForgotById(forgotId).orElseThrow(() -> new EntityNotFoundException("Cannot find forgot with id = " + forgotId));
            forgot.setStatus(forgotDto.isStatus());

            Forgot forgotUpdated = forgotService.saveForgot(forgot);

            return new ResponseEntity<>(forgotUpdated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
