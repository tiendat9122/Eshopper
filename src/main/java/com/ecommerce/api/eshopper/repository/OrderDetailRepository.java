package com.ecommerce.api.eshopper.repository;

import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrders(Orders orders);

}
