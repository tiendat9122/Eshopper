package com.ecommerce.api.eshopper.service.advertise_service;

import com.ecommerce.api.eshopper.entity.Advertise;
import com.ecommerce.api.eshopper.repository.AdvertiseRepository;
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
public class AdvertiseService implements IAdvertiseService {

    private final AdvertiseRepository advertiseRepository;

    // Get and find
    public List<Advertise> getAllAdvertise() {
        return advertiseRepository.findAll();
    }

    public Optional<Advertise> findAdvertiseById(Long id) {
        return advertiseRepository.findById(id);
    }

    // Save and update
    public Advertise saveAdvertise(Advertise advertise) {
        return advertiseRepository.save(advertise);
    }

    // Delete
    public void deleteAdvertise(Advertise advertise) {
        advertiseRepository.delete(advertise);
    }

    public void deleteAdvertiseById(Long id) {
        advertiseRepository.deleteById(id);
    }

}
