package com.ecommerce.api.eshopper.controller.user.product_controller;
import com.ecommerce.api.eshopper.dto.ProductDto;
import com.ecommerce.api.eshopper.entity.Author;
import com.ecommerce.api.eshopper.entity.Category;
import com.ecommerce.api.eshopper.entity.OrderDetail;
import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.Product;
import com.ecommerce.api.eshopper.entity.User;
import com.ecommerce.api.eshopper.service.author_service.IAuthorService;
import com.ecommerce.api.eshopper.service.category_service.ICategoryService;
import com.ecommerce.api.eshopper.service.orderdetail_service.IOrderDetailService;
import com.ecommerce.api.eshopper.service.orders_service.IOrdersService;
import com.ecommerce.api.eshopper.service.product_service.IProductService;
import com.ecommerce.api.eshopper.service.user_service.IUserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/product")
public class UserProductApi {

    private final IProductService productService;

    private final ICategoryService categoryService;

    private final IAuthorService authorService;

    private final IOrderDetailService orderDetailService;

    private final IOrdersService ordersService;

    private final IUserService userService;

    @Value("${file.upload-dir}")
    private String FILE_DIRECTORY;

    @GetMapping("/get")
    public ResponseEntity<?> getProduct(@RequestParam(name = "id", required = false) Long id) {
        
        try {
            if (id != null) {
                Product product = productService.findProductById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find product by id = " + id));
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                List<Product> products = productService.findAllProductIsActive();
                return new ResponseEntity<>(products, HttpStatus.OK);
            }
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/find")
    public ResponseEntity<?> findProductByNameAndAuthor(@RequestParam(name = "name", required = false) String name) {

        try {
            if(name != null) {
                List<Product> products = productService.findProductByKeyWord(name);
                return new ResponseEntity<>(products, HttpStatus.OK);
            } 
            List<Product> products = productService.findAllProductIsActive();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/recommend")
    public ResponseEntity<?> getProductRecommend() {

        try {
            List<Product> productRecommends = productService.findProductRandom();
            return new ResponseEntity<>(productRecommends, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/download/{picture}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getPicture(@PathVariable(name = "picture") String picture) {

        if (!picture.equals("") || picture != null) {
            try {
                Path filename = Paths.get(FILE_DIRECTORY, "products", picture);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/png"))
                        .body(byteArrayResource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();

    }
    
    @PutMapping("/addcart")
    public ResponseEntity<?> addProductToCart(@RequestParam(name = "userId") Long userId, 
                                                @RequestParam(name = "productId") Long productId, 
                                                @RequestParam(name = "quantity") Long quantity) {
        
        try {
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find user with id = " + userId));
            Product product = productService.findProductById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find product with id = " + productId));
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
