package com.hirehub.bookingsystem.service.impl;

import com.hirehub.bookingsystem.dto.request.RegisterRequest;
import com.hirehub.bookingsystem.dto.response.UserResponse;
import com.hirehub.bookingsystem.entities.User;
import com.hirehub.bookingsystem.enums.Role;
import com.hirehub.bookingsystem.mappers.UserMapper;
import com.hirehub.bookingsystem.repositories.UserRepository;
import com.hirehub.bookingsystem.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse findById(Long id) {
        return userMapper.toResponse(getUserById(id));
    }

    @Override
    @Transactional
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return userMapper.toResponse(user);
    }

    // Package-private helper reused by other services
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

}
