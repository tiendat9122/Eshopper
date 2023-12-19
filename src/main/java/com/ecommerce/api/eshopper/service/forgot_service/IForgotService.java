package com.ecommerce.api.eshopper.service.forgot_service;

import com.ecommerce.api.eshopper.entity.Feedback;
import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.User;

import java.util.List;
import java.util.Optional;

public interface IForgotService {

    // Get and find
    List<Forgot> getAllForgot();

    Optional<Forgot> findForgotById(Long id);

    List<Forgot> findForgotByUserAndStatusTrue(User user);

    // Save and update
    Forgot saveForgot(Forgot forgot);

    // Delete
    void deleteForgot(Forgot forgot);

    void deleteForgotById(Long id);

}
