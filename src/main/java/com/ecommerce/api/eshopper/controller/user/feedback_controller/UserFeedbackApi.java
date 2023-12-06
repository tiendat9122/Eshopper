package com.ecommerce.api.eshopper.controller.user.feedback_controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.api.eshopper.dto.FeedbackDto;
import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.feedback_service.IFeedbackService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/feedback")
@RequiredArgsConstructor
public class UserFeedbackApi {
    
    private final IFeedbackService feedbackService;

    private final IUserService userService;

    private final IProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getProductFeedback(@RequestParam(name = "productId") Long productId) {

        try {
            if(productId != null) {
                List<Feedback> feedbacks = feedbackService.getFeedbackProduct(productId);
                return new ResponseEntity<>(feedbacks, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Coudn't found any feedback about this product", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertProductFeedback(@RequestBody FeedbackDto feedbackDto) {
        try {
            Feedback feedback = new Feedback();
            feedback.setComment(feedbackDto.getComment());

            feedback.setActive(true);

            LocalDateTime now = LocalDateTime.now();
            feedback.setCommentDate(now);

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

    
}
