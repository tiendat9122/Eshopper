package com.ecommerce.api.eshopper.service.role_service;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    // Get and find
    List<Role> findAllRole();

    Optional<Role> findRoleById(Long id);

    // Save and update
    User saveUser(User user);

    Role saveRole(Role role);

    void addUserToRole(String username, String rolename);

    // Remove
    void deleteRole(Role role);

}
