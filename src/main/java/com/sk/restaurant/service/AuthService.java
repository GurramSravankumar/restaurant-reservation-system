package com.sk.restaurant.service;

import com.sk.restaurant.dto.AuthResponse;
import com.sk.restaurant.dto.LoginRequest;
import com.sk.restaurant.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
