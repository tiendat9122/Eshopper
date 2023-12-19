package com.ecommerce.api.eshopper.service.advertise_service;

import com.ecommerce.api.eshopper.entity.Advertise;
import com.ecommerce.api.eshopper.entity.Slide;

import java.util.List;
import java.util.Optional;

public interface IAdvertiseService {

    // Get and find
    List<Advertise> getAllAdvertise();

    Optional<Advertise> findAdvertiseById(Long id);

    // Save and update
    Advertise saveAdvertise(Advertise advertise);

    // Delete
    void deleteAdvertise(Advertise advertise);

    void deleteAdvertiseById(Long id);

}
