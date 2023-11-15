package com.ecommerce.api.eshopper.service.user_service;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    // Get and find
    List<User> findAllUser();

    Optional<User> findUserById(Long id);

    User findUserByEmail(String email);

    // Save and update
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String rolename);

    // Remove
    void deleteUser(User user);

    void deleteUserById(Long id);

}
