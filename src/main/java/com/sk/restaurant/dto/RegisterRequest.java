package com.sk.restaurant.dto;

import com.sk.restaurant.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
