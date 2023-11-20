package com.ecommerce.api.eshopper.service.orders_service;

import com.ecommerce.api.eshopper.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface IOrdersService {

    // Get and find
    List<Orders> getAllOrders();

    Optional<Orders> findOrdersById(Long id);

    // Save and update
    Orders saveOrders(Orders orders);

    // Remove
    void deleteOrders(Orders orders);

    void deleteOrdersById(Long id);

}
