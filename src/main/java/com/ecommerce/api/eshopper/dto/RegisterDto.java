package com.ecommerce.api.eshopper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {

    private String full_name;

    private String user_name;

    private String email;

    private String password;

}
