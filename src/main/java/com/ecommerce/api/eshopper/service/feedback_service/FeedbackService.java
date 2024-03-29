package com.ecommerce.api.eshopper.service.feedback_service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.repository.FeedbackRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FeedbackService implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;

    // Get and find
    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public Page<Feedback> getFeedbackProduct(Long productId, Pageable pageable) {
        return feedbackRepository.findByProduct_Id(productId, pageable);
    }

    @Override
    public Optional<Feedback> findFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    // Save and update
    @Override
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Delete
    @Override
    public void deleteFeedback(Feedback feedback) {
        feedbackRepository.delete(feedback);
    }

    @Override
    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
    }
    
} 