package com.ecommerce.api.eshopper.service.user_service;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import jakarta.transaction.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAllUser();
    Optional<User> findUserById(Long id);
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String rolename);
    void deleteUser(User user);
    void deleteUserById(Long id);
}
