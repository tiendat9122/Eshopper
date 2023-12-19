package com.ecommerce.api.eshopper.service.feedback_service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Product;

public interface IFeedbackService {

    // Get and find
    List<Feedback> getAllFeedback();
    
    List<Feedback> getFeedbackProduct(Long productId);

    Optional<Feedback> findFeedbackById(Long id);

    // Save and update
    Feedback saveFeedback(Feedback feedback);

    // Delete
    void deleteFeedback(Feedback feedback);

    void deleteFeedbackById(Long id);

} 