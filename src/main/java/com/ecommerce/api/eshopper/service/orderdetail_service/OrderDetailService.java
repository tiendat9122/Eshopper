package com.ecommerce.api.eshopper.service.orderdetail_service;

import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.repository.OrderDetailRepository;
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
public class OrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    // Get and find
    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findOrderDetailById(Long id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrders(Orders orders) {
        return orderDetailRepository.findByOrders(orders);
    }

    // Save and update
    @Override
    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public void deleteOrderDetailById(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
