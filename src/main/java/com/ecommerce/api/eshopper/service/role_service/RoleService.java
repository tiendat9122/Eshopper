package com.ecommerce.api.eshopper.service.role_service;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.repository.RoleRepository;
import com.ecommerce.api.eshopper.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService implements IRoleService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addUserToRole(String username, String rolename) {
        User user = userRepository.findByEmail(username);
        Role role = roleRepository.findByName(rolename);
        role.getUser().add(user);
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }
}
