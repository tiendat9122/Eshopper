package com.ecommerce.api.eshopper.service.feedback_service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Product;

public interface IFeedbackService {
    
    List<Feedback> getAllFeedback();
    
    List<Feedback> getFeedbackProduct(Long productId);

    Optional<Feedback> findFeedbackById(Long id);

    Feedback saveFeedback(Feedback feedback);

    void deleteFeedback(Feedback feedback);

    void deleteFeedbackById(Long id);

} 