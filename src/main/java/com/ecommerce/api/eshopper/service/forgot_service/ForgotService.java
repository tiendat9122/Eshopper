package com.ecommerce.api.eshopper.service.forgot_service;

import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.repository.ForgotRepository;
import com.ecommerce.api.eshopper.service.user_service.UserService;
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
public class ForgotService implements IForgotService {

    private final ForgotRepository forgotRepository;

    // Get and find
    public List<Forgot> getAllForgot() {
        return forgotRepository.findAll();
    }

    public Optional<Forgot> findForgotById(Long id) {
        return forgotRepository.findById(id);
    }

    public List<Forgot> findForgotByUserAndStatusTrue(User user) {
        return forgotRepository.findByUserAndStatus(user);
    }

    // Save and update
    public Forgot saveForgot(Forgot forgot) {
        return forgotRepository.save(forgot);
    }

    // Delete
    public void deleteForgot(Forgot forgot) {
        forgotRepository.delete(forgot);
    }

    public void deleteForgotById(Long id) {
        forgotRepository.deleteById(id);
    }

}
