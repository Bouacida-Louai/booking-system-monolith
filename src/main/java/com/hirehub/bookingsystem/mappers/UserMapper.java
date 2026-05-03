package com.hirehub.bookingsystem.mappers;


import com.hirehub.bookingsystem.dto.request.RegisterRequest;
import com.hirehub.bookingsystem.dto.response.UserResponse;
import com.hirehub.bookingsystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    // RegisterRequest → User
    // password is set manually in service (after encoding)
    // role is set manually in service
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role",     ignore = true)
    @Mapping(target = "enabled",  ignore = true)
    @Mapping(target = "bookings", ignore = true)
    User toEntity(RegisterRequest request);

    // User → UserResponse
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserResponse toResponse(User user);
}