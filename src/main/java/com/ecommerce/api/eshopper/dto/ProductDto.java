package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String name;

    private double retail;

    private Long inventory;

    private MultipartFile picture;

    private Long authorId;

    private List<Long> categoryIds;

}
