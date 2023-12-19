package com.ecommerce.api.eshopper.service.slide_service;

import com.ecommerce.api.eshopper.entity.Slide;

import java.util.List;
import java.util.Optional;

public interface ISlideService {

    // Get and find
    List<Slide> getAllSlide();

    Optional<Slide> findSlideById(Long id);

    // Save and update
    Slide saveSlide(Slide slide);

    // Delete
    void deleteSlide(Slide slide);

    void deleteSlideById(Long id);

}
