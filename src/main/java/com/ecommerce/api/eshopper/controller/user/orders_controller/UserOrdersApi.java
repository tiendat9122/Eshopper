package com.ecommerce.api.eshopper.controller.user.orders_controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.orderdetail_service.IOrderDetailService;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class UserOrdersApi {
    
    private final IOrdersService ordersService;

    private final IOrderDetailService orderDetailService;

    private final IUserService userService;

    private final IProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserCart(@RequestParam(name = "userId") Long userId) {

        try {
            if (userId != null) {
                User user = userService.findUserById(userId).orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));

                List<Orders> ordersList = ordersService.findOrdersByUserAndStateFalse(user);

                List<OrderDetail> orderDetails = orderDetailService.getOrderDetailByOrders(ordersList.get(0));
                return new ResponseEntity<>(orderDetails, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/addquantity")
    public ResponseEntity<?> addQuantityOrderDetail(@RequestParam(name = "orderDetailId") Long orderDetailId,
                                                    @RequestParam(name = "productId") Long productId) {

        try {
            OrderDetail orderDetail =  orderDetailService.findOrderDetailById(orderDetailId).orElseThrow(() -> new EntityNotFoundException("Cannot find orderdetail with id = " + orderDetailId));
            Long addQuantity = orderDetail.getQuantity() + 1L;

            Product product = productService.findProductById(productId).orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + productId));
            double price = addQuantity * product.getRetail();

            orderDetail.setQuantity(addQuantity);
            orderDetail.setPrice(price);

            OrderDetail addOrderDetail = orderDetailService.saveOrderDetail(orderDetail);

            Orders orders = ordersService.findOrdersById(orderDetail.getOrders().getId()).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + orderDetail.getOrders().getId()));
            List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrders(orders);
            double totalPrice = 0;
            for(OrderDetail item : orderDetailList) {
                totalPrice += item.getPrice();
            }
            orders.setTotalPrice(totalPrice);
            orders.setTotalBill(totalPrice + orders.getTransport());
            Orders ordersUpdated = ordersService.saveOrders(orders);


            return new ResponseEntity<>(addOrderDetail, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/subtractquantity")
    public ResponseEntity<?> subtractQuantityOrderDetail(@RequestParam(name = "orderDetailId") Long orderDetailId,
                                                    @RequestParam(name = "productId") Long productId) {

        try {
            OrderDetail orderDetail =  orderDetailService.findOrderDetailById(orderDetailId).orElseThrow(() -> new EntityNotFoundException("Cannot find orderdetail with id = " + orderDetailId));

            if(orderDetail.getQuantity() == 1) {
                orderDetailService.deleteOrderDetail(orderDetail);
                return new ResponseEntity<>("Deleted orderdetail successfully", HttpStatus.OK);
            } else {
                Long subtractQuantity = orderDetail.getQuantity() - 1L;
    
                Product product = productService.findProductById(productId).orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + productId));
                double price = subtractQuantity * product.getRetail();
    
                orderDetail.setQuantity(subtractQuantity);
                orderDetail.setPrice(price);
    
                OrderDetail subtractOrderDetail = orderDetailService.saveOrderDetail(orderDetail);

                Orders orders = ordersService.findOrdersById(orderDetail.getOrders().getId()).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + orderDetail.getOrders().getId()));
                List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrders(orders);
                double totalPrice = 0;
                for(OrderDetail item : orderDetailList) {
                    totalPrice += item.getPrice();
                }
                orders.setTotalPrice(totalPrice);
                orders.setTotalBill(totalPrice + orders.getTransport());
                Orders ordersUpdated = ordersService.saveOrders(orders);

                return new ResponseEntity<>(subtractOrderDetail, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserOrdersFalse(@RequestParam(name = "id") Long id) {

        try {
            OrderDetail orderDetail = orderDetailService.findOrderDetailById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find orderdetail with id = " + id));
            orderDetailService.deleteOrderDetail(orderDetail);
            
            Orders orders = ordersService.findOrdersById(orderDetail.getOrders().getId()).orElseThrow(() -> new EntityNotFoundException("Cannot find orders with id = " + orderDetail.getOrders().getId()));
            List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrders(orders);
            double totalPrice = 0;
            for(OrderDetail item : orderDetailList) {
                totalPrice += item.getPrice();
            }
            orders.setTotalPrice(totalPrice);
            orders.setTotalBill(totalPrice + orders.getTransport());
            Orders ordersUpdated = ordersService.saveOrders(orders);

            return new ResponseEntity<>("Deleted orderdetail successfully", HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCartbyUser(@RequestParam(name = "userId") Long userId) {
        
        try {
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find user with userId = " + userId));
            List<Orders> ordersList = ordersService.findOrdersByUserAndStateFalse(user);
            Orders orders = ordersList.get(0);
            
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
