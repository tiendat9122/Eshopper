package com.ecommerce.api.eshopper.service.orderdetail_service;

import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailService {

    // Get and find
    List<OrderDetail> getAllOrderDetail();

    Optional<OrderDetail> findOrderDetailById(Long id);

    List<OrderDetail> getOrderDetailByOrders(Orders orders);

    // Save and update
    OrderDetail saveOrderDetail(OrderDetail orderDetail);

    // Remove
    void deleteOrderDetail(OrderDetail orderDetail);

    void deleteOrderDetailById(Long id);

}
