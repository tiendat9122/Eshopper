package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Role;
import com.ecommerce.api.eshopper.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT r FROM Role r JOIN r.user u WHERE u.email = :email")
    Set<Role> findRolesByEmail(@Param("email") String email);

    @Query("SELECT r FROM Role r JOIN r.user u WHERE u.email = :email")
    @Transactional
    Set<Role> findRoles(@Param("email") String email);

}
