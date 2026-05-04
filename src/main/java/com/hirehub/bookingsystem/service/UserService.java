package com.hirehub.bookingsystem.service;


import com.hirehub.bookingsystem.dto.request.RegisterRequest;
import com.hirehub.bookingsystem.dto.response.UserResponse;
import org.springframework.stereotype.Service;


public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse findById(Long id);
    UserResponse findByEmail(String email);
}
