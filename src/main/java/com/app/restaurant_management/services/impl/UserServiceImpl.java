package com.app.restaurant_management.services.impl;

import com.app.restaurant_management.commons.constants.MessageResponseConstants;
import com.app.restaurant_management.commons.dto.request.user.LoginRequest;
import com.app.restaurant_management.commons.dto.request.user.RegisterRequest;
import com.app.restaurant_management.commons.enums.RoleName;
import com.app.restaurant_management.commons.exception.ApiErrorException;
import com.app.restaurant_management.config.security.JwtTokenProvider;
import com.app.restaurant_management.models.Role;
import com.app.restaurant_management.models.User;
import com.app.restaurant_management.repository.RoleRepository;
import com.app.restaurant_management.repository.UserRepository;
import com.app.restaurant_management.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public String userLogin(LoginRequest login){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getUsername(), login.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String userRegister(RegisterRequest register) {
        if(userRepository.existsByUsername(register.getUsername())){
            throw new ApiErrorException(HttpStatus.BAD_REQUEST, MessageResponseConstants.USERNAME_ALREADY_EXISTS_RESPONSE);
        }

        User user = mapToUser(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findRoleByRoleName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role name not found!"));
        Role adminRole = roleRepository.findRoleByRoleName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role name not found!"));
        if(register.getIsAdmin()){
            roles.add(adminRole);
        }else{
            roles.add(userRole);
        }

        user.setRoles(roles);
        userRepository.save(user);

        return MessageResponseConstants.REGISTER_RESPONSE;

    }

    private User mapToUser(RegisterRequest request) {
        return modelMapper.map(request, User.class);
    }
}
