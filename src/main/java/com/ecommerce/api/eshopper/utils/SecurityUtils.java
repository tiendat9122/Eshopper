package com.ecommerce.api.eshopper.utils;

import com.ecommerce.api.eshopper.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getPrinciple() {
        return (User) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
    }
}
