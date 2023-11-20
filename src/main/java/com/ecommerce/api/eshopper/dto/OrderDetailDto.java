package com.ecommerce.api.eshopper.dto;

import com.ecommerce.api.eshopper.entity.Orders;
import com.ecommerce.api.eshopper.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {

    private Long quantity;

    private Long productId;

    private Long ordersId;

}
