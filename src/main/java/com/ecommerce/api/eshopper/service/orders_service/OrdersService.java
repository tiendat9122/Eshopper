package com.ecommerce.api.eshopper.service.orders_service;

import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrdersService implements IOrdersService {

    private final OrdersRepository ordersRepository;

    // Get and find
    @Override
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Override
    public Optional<Orders> findOrdersById(Long id) {
        return ordersRepository.findById(id);
    }

    @Override
    public List<Orders> findOrdersByUserAndStateFalse(User user) {
        return ordersRepository.findByUserAndStateFalse(user);
    }

    // Save and update
    @Override
    public Orders saveOrders(Orders orders) {
        return ordersRepository.save(orders);
    }

    // Delete
    @Override
    public void deleteOrders(Orders orders) {
        ordersRepository.delete(orders);
    }

    @Override
    public void deleteOrdersById(Long id) {
        ordersRepository.deleteById(id);
    }
}
