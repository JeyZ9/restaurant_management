package com.app.restaurant_management.controllers;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.constants.PathConstants;
import com.app.restaurant_management.commons.dto.request.user.LoginRequest;
import com.app.restaurant_management.commons.dto.request.user.RegisterRequest;
import com.app.restaurant_management.commons.dto.response.JwtAuthResponse;
import com.app.restaurant_management.commons.exception.AuthenticationFailException;
import com.app.restaurant_management.commons.exception.CustomException;
import com.app.restaurant_management.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstants.AUTH)
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService){
        this.userService = userService;
    }

//    @PostMapping(path = {"/login", "/signin"})
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) throws CustomException {
        try {
            String token = userService.userLogin(request);
            JwtAuthResponse response = new JwtAuthResponse();
            response.setAccessToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (BadCredentialsException err){
            throw new BadCredentialsException(MessageResponseConstants.LOGIN_ERROR_RESPONSE, err);
        } catch (Exception err){
            throw new CustomException(MessageResponseConstants.LOGIN_ERROR_RESPONSE);
        }
    }

//    @PostMapping(path = {"/register", "/signup"})
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest register) throws AuthenticationFailException  {
        String user = userService.userRegister(register);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
