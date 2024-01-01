package com.ecommerce.api.eshopper.dto;

import com.ecommerce.api.eshopper.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDto {

    private Product product;

    private Long totalQuantity;

}
