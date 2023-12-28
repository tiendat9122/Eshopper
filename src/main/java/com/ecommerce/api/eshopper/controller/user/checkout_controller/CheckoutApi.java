package com.ecommerce.api.eshopper.controller.user.checkout_controller;

import com.ecommerce.api.eshopper.dto.OrdersDto;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/checkout")
public class CheckoutApi {

    private final IOrdersService ordersService;

    @PutMapping("/pay")
    public ResponseEntity<?> userCheckout(@RequestBody OrdersDto ordersDto) {

        try {

            Orders orders = ordersService.findOrdersById(ordersDto.getId()).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + ordersDto.getId()));
            orders.setAddress(ordersDto.getAddress());
            orders.setPhone_number(ordersDto.getPhone_number());
            orders.setFull_name(ordersDto.getFull_name());
            orders.setState(true);

            Orders orderUpdated = ordersService.saveOrders(orders);

            return new ResponseEntity<>(orderUpdated, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
