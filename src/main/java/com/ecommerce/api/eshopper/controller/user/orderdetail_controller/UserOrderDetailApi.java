package com.ecommerce.api.eshopper.controller.user.orderdetail_controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.api.eshopper.dto.OrderDetailDto;
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
@RequiredArgsConstructor
@RequestMapping("/user/orderdetail")
public class UserOrderDetailApi {

    private final IOrderDetailService orderDetailService;

    private final IOrdersService ordersService;

    private final IProductService productService;

    private final IUserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getOrdersByUserAndActiveFalse(@RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "productId") Long productId) {

        try {
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
            Product product = productService.findProductById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + productId));
            Long quantity = 1L;
            double price = product.getRetail() * quantity;
            List<Orders> ordersList = ordersService.findOrdersByUserAndStateFalse(user);
            if (!ordersList.isEmpty()) {
                Orders orders = ordersList.get(0);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrders(orders);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(price);

                OrderDetail orderDetailInserted = orderDetailService.saveOrderDetail(orderDetail);

                List<OrderDetail> orderDetails = orderDetailService.getOrderDetailByOrders(orders);
                double totalPrice = 0;
                for(OrderDetail item : orderDetails) {
                    totalPrice += item.getPrice();
                }
                
                orders.setTotalPrice(totalPrice);
                orders.setTotalBill(orders.getTotalPrice() + orders.getTransport());
                ordersService.saveOrders(orders);

                return new ResponseEntity<>(orderDetailInserted, HttpStatus.OK);
            } else {
                Orders ordersNew = new Orders();
                ordersNew.setState(false);
                ordersNew.setTransport(25000);
                ordersNew.setNote("Giỏ hàng");
                LocalDateTime now = LocalDateTime.now();
                ordersNew.setOrderDate(now);
                ordersNew.setUser(user);

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrders(ordersNew);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice(price);

                ordersNew.setTotalPrice(price);
                ordersNew.setTotalBill(ordersNew.getTotalPrice() + ordersNew.getTransport());
                ordersService.saveOrders(ordersNew);
                
                OrderDetail orderDetailInserted = orderDetailService.saveOrderDetail(orderDetail);

                return new ResponseEntity<>(orderDetailInserted, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

}
