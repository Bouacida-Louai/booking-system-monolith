package com.hirehub.bookingsystem.mappers;


import com.hirehub.bookingsystem.dto.request.RoomRequest;
import com.hirehub.bookingsystem.dto.response.RoomResponse;
import com.hirehub.bookingsystem.entities.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface RoomMapper {

    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "available", constant = "true")
    @Mapping(target = "bookings", ignore = true)
    Room toEntity(RoomRequest request);

    RoomResponse toResponse(Room room);

    // For update — merges request into existing entity
    @Mapping(target = "id",       ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntity(RoomRequest request, @MappingTarget Room room);
}
