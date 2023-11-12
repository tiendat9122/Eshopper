package com.ecommerce.api.eshopper.dto;

import com.ecommerce.api.eshopper.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String full_name;

    private String user_name;

    private String email;

    private String password;

    private List<Long> roleIds;

}
