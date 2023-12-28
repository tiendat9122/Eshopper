package com.ecommerce.api.eshopper.service.slide_service;

import com.ecommerce.api.eshopper.entity.Slide;
import com.ecommerce.api.eshopper.repository.SlideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SlideService implements ISlideService {

    private final SlideRepository slideRepository;

    // Get and find
    @Override
    public List<Slide> getAllSlide() {
        return slideRepository.findAll();
    }

    @Override
    public Optional<Slide> findSlideById(Long id) {
        return slideRepository.findById(id);
    }

    @Override
    public List<Slide> getAllSlideActiveTrue() {
        return slideRepository.findByActiveTrue();
    }

    // Save and update
    @Override
    public Slide saveSlide(Slide slide) {
        return slideRepository.save(slide);
    }

    // Delete
    @Override
    public void deleteSlide(Slide slide) {
        slideRepository.delete(slide);
    }

    @Override
    public void deleteSlideById(Long id) {
        slideRepository.deleteById(id);
    }

}
