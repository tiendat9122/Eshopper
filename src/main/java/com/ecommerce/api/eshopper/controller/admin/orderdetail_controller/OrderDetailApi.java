package com.ecommerce.api.eshopper.controller.admin.orderdetail_controller;

import com.ecommerce.api.eshopper.dto.OrderDetailDto;
import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.service.orderdetail_service.IOrderDetailService;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderdetail")
public class OrderDetailApi {

    private final IOrderDetailService orderDetailService;

    private final IProductService productService;

    private final IOrdersService ordersService;

    @GetMapping("/get")
    public ResponseEntity<?> getOrderDetail(@RequestParam(name = "id", required = false) Long id) {

        try {
            if(id != null) {
                OrderDetail orderDetail = orderDetailService.findOrderDetailById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find order detail with id = " + id));
                return new ResponseEntity<>(orderDetail, HttpStatus.OK);
            } else {
                List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetail();
                return new ResponseEntity<>(orderDetails, HttpStatus.OK);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {

        try {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(orderDetailDto.getQuantity());

            Long productId = orderDetailDto.getProductId();
            Product product = productService.findProductById(productId).orElseThrow();
            orderDetail.setProduct(product);

            double price = product.getRetail() * orderDetailDto.getQuantity();
            orderDetail.setPrice(price);

            Long ordersId = orderDetailDto.getOrdersId();
            Orders orders = ordersService.findOrdersById(ordersId).orElseThrow();
            orderDetail.setOrders(orders);

            OrderDetail orderDetailInserted = orderDetailService.saveOrderDetail(orderDetail);

            return new ResponseEntity<>(orderDetailInserted, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrderDetail(@RequestParam(name = "id") Long id, @RequestBody OrderDetailDto orderDetailDto) {

        try {
            OrderDetail orderDetail = orderDetailService.findOrderDetailById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find orderdetail with id = " + id));
            orderDetail.setQuantity(orderDetailDto.getQuantity());

            Long productId = orderDetailDto.getProductId();
            Product product = productService.findProductById(productId).orElseThrow();
            orderDetail.setProduct(product);

            double price = product.getRetail() * orderDetailDto.getQuantity();
            orderDetail.setPrice(price);

            Long ordersId = orderDetailDto.getOrdersId();
            Orders orders = ordersService.findOrdersById(id).orElseThrow();
            orderDetail.setOrders(orders);

            OrderDetail orderDetailUpdated = orderDetailService.saveOrderDetail(orderDetail);

            return new ResponseEntity<>(orderDetailUpdated, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrderDetail(@RequestParam(name = "id") Long id) {

        try {
            OrderDetail orderDetail = orderDetailService.findOrderDetailById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find orderdetail with id = " + id));
            orderDetailService.deleteOrderDetail(orderDetail);
            return new ResponseEntity<>("OrderDetail deleted successfully!", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
