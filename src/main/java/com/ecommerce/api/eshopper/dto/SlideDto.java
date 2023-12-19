package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlideDto {

    private Long id;

    private String name;

    private String theme;

    private MultipartFile picture_file;

    private String picture;

    private boolean active;

}
