package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserAndStateFalse(User user);

}
