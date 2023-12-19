package com.ecommerce.api.eshopper.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String full_name;

    private String user_name;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth_day;

    private String address;

    private String phone_number;

    private MultipartFile avatar_file;

    private String avatar;

    private String email;

    private String password;

    private String newPassword;

    private boolean active;

    private List<Long> roleIds;

}
