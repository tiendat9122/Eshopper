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

    private Long id;

    private String name;

    private double retail;

    private Long inventory;

    private MultipartFile picture_file;

    private String picture;

    private String summary;

    private boolean hot;

    private boolean active;

    private Long authorId;

    private List<Long> categoryIds;

}
