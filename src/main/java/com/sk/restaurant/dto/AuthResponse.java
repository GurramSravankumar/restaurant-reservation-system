package com.sk.restaurant.dto;

import com.sk.restaurant.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private String email;
    private Role role;
    private String name;
}
