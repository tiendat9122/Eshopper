package com.ecommerce.api.eshopper.service.role_service;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import jakarta.transaction.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<Role> findAllRole();
    User saveUser(User user);
    Role saveRole(Role role);
    void addUserToRole(String username, String rolename);
    Optional<Role> findRoleById(Long id);
    void deleteRole(Role role);
}
