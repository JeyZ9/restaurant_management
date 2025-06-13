package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.dto.request.user.LoginRequest;
import com.app.restaurant_management.commons.dto.request.user.RegisterRequest;

public interface UserService {
    String userLogin(LoginRequest login);

    String userRegister(RegisterRequest register);
}
