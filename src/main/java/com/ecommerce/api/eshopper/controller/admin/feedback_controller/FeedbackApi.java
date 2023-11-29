package com.ecommerce.api.eshopper.controller.admin.feedback_controller;

import com.ecommerce.api.eshopper.dto.FeedbackDto;
import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.feedback_service.IFeedbackService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackApi {

    private final IFeedbackService feedbackService;

    private final IProductService productService;

    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getFeedback(@RequestParam(name = "id", required = false) Long id) {
        try {
            if (id != null) {
                Feedback feedback = feedbackService.findFeedbackById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find feedback with id = " + id));
                return new ResponseEntity<>(feedback, HttpStatus.OK);
            } else {
                List<Feedback> feedbacks = feedbackService.getAllFeedback();
                return new ResponseEntity<>(feedbacks, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertFeedback(@RequestBody FeedbackDto feedbackDto) {
        try {
            Feedback feedback = new Feedback();
            feedback.setComment(feedbackDto.getComment());
            feedback.setActive(feedbackDto.isActive());

//            LocalDateTime now = LocalDateTime.now();
//            feedback.setCommentDate(now);

            feedback.setCommentDate(feedbackDto.getCommentDate());

            Long productId = feedbackDto.getProductId();
            Product product = productService.findProductById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + productId));
            feedback.setProduct(product);

            Long userId = feedbackDto.getUserId();
            User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
            feedback.setUser(user);

            Feedback feedbackInserted = feedbackService.saveFeedback(feedback);

            return new ResponseEntity<>(feedbackInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/active")
    public ResponseEntity<?> activeFeedback(@RequestBody FeedbackDto feedbackDto) {

        try {
            Long feedbackId = feedbackDto.getId();
            Feedback feedback = feedbackService.findFeedbackById(feedbackId).orElseThrow(() -> new EntityNotFoundException("Cannot find feedback with id = " + feedbackId));
            feedback.setActive(feedbackDto.isActive());

            Feedback feedbackActived = feedbackService.saveFeedback(feedback);

            return new ResponseEntity<>(feedbackActived, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateFeedback(@RequestParam(name = "id") Long id, @RequestBody FeedbackDto feedbackDto) {

        try {
            Feedback feedback = feedbackService.findFeedbackById(id).orElseThrow(() -> new EntityNotFoundException("Cannot fin feedback with id = " + id));
            feedback.setComment(feedbackDto.getComment());
            feedback.setCommentDate(feedbackDto.getCommentDate());
            feedback.setActive(feedbackDto.isActive());

            Long productId = feedbackDto.getProductId();
            Product product = productService.findProductById(productId).orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + id));
            feedback.setProduct(product);

            Long userId = feedbackDto.getUserId();
            User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
            feedback.setUser(user);

            Feedback feedbackUpdate = feedbackService.saveFeedback(feedback);

            return new ResponseEntity<>(feedbackUpdate, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFeedback(@RequestParam(name = "id") Long id) {
        
        try {
            if(id != null) {
                Feedback feedback = feedbackService.findFeedbackById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find feedback with id = " + id));
                feedbackService.deleteFeedback(feedback);
                return new ResponseEntity<>("Feedback deleted successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot deleted feedback", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

}
