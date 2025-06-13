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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstants.AUTH)
public class UserController {

    private final UserServiceImpl userService;
    private final View error;

    @Autowired
    public UserController(UserServiceImpl userService, View error){
        this.userService = userService;
        this.error = error;
    }

//    @PostMapping(path = {"/login", "/signin"})
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) throws CustomException {
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest register, BindingResult bindingResult) throws CustomException, AuthenticationFailException {
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors()
                    .stream().map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            String user = userService.userRegister(register);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch(Exception err) {
            throw new CustomException(MessageResponseConstants.SERVER_ERROR_RESPONSE);
        }
    }
}
