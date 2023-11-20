package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    private String name;

    private String displayName;

    private List<Long> userIds;

}
