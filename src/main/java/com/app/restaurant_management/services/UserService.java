package com.app.restaurant_management.services;

import com.app.restaurant_management.commons.dto.request.user.LoginRequest;
import com.app.restaurant_management.commons.dto.request.user.RegisterRequest;
import com.app.restaurant_management.commons.exception.AuthenticationFailException;

public interface UserService {
    String userLogin(LoginRequest login);

    String userRegister(RegisterRequest register) throws AuthenticationFailException;
}
