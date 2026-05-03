package com.hirehub.bookingsystem.mappers;


import com.hirehub.bookingsystem.dto.request.RestaurantTableRequest;
import com.hirehub.bookingsystem.dto.response.RestaurantTableResponse;
import com.hirehub.bookingsystem.entities.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface RestaurantTableMapper {

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "bookings", ignore = true)
    RestaurantTable toEntity(RestaurantTableRequest request);

    RestaurantTableResponse toResponse(RestaurantTable table);

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntity(RestaurantTableRequest request, @MappingTarget RestaurantTable table);
}
