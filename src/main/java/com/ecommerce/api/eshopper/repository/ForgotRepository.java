package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Forgot;
import com.ecommerce.api.eshopper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgotRepository extends JpaRepository<Forgot, Long> {

    @Query("SELECT f FROM Forgot f WHERE f.user = :user AND f.status = true")
    List<Forgot> findByUserAndStatus(@Param("user") User user);

}
