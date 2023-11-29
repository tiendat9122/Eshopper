package com.ecommerce.api.eshopper.controller.admin.orders_controller;

import com.ecommerce.api.eshopper.dto.OrdersDto;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.orderdetail_service.IOrderDetailService;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersApi {

    private final IOrdersService ordersService;

    private final IOrderDetailService orderDetailService;

    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getOrders(@RequestParam(name = "id", required = false) Long id) {
        try {
            if (id != null) {
                Orders orders = ordersService.findOrdersById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find category with id = " + id));
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                    List<Orders> orders = ordersService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    } catch (EntityNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/state")
    public ResponseEntity<?> stateOrders(@RequestBody OrdersDto ordersDto) {

        try {
            Long ordersId = ordersDto.getId();
            Orders orders = ordersService.findOrdersById(ordersId).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + ordersId));
            orders.setState(ordersDto.isState());
            
            Orders ordersStated = ordersService.saveOrders(orders);
            return new ResponseEntity<>(ordersStated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertOrders(@RequestBody OrdersDto ordersDto) {

        try {
            Orders orders = new Orders();
            orders.setOrderDate(ordersDto.getOrderDate());
            orders.setNote(ordersDto.getNote());
            orders.setState(ordersDto.isState());

            Long userId = ordersDto.getUserId();
            User user = userService.findUserById(userId).orElseThrow();

            orders.setUser(user);

            Orders ordersInserted = ordersService.saveOrders(orders);

            return new ResponseEntity<>(ordersInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrders(@RequestParam(name = "id") Long id, @RequestBody OrdersDto ordersDto) {

        try {
            Orders orders = ordersService.findOrdersById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + id));
            orders.setOrderDate(ordersDto.getOrderDate());
            orders.setState(ordersDto.isState());
            orders.setNote(ordersDto.getNote());

            Long userId = ordersDto.getUserId();
            User user = userService.findUserById(userId).orElseThrow();

            orders.setUser(user);

            Orders ordersInserted = ordersService.saveOrders(orders);

            return new ResponseEntity<>(ordersInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrders(@RequestParam(name = "id") Long id) {

        try {
            Orders orders = ordersService.findOrdersById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " +id));
            ordersService.deleteOrders(orders);
            return new ResponseEntity<>("Orders deleted successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
