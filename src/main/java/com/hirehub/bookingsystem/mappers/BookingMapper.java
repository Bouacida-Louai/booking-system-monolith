package com.hirehub.bookingsystem.mappers;

import com.hirehub.bookingsystem.dto.response.BookingResponse;
import com.hirehub.bookingsystem.entities.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BookingMapper {

    @Mapping(target = "userId",
            expression = "java(booking.getUser().getId())")
    @Mapping(target = "userFullName",
            expression = "java(booking.getUser().getFirstName() + ' ' + booking.getUser().getLastName())")
    @Mapping(target = "roomId",
            expression = "java(booking.getRoom() != null ? booking.getRoom().getId() : null)")
    @Mapping(target = "roomNumber",
            expression = "java(booking.getRoom() != null ? booking.getRoom().getRoomNumber() : null)")
    @Mapping(target = "tableId",
            expression = "java(booking.getRestaurantTable() != null ? booking.getRestaurantTable().getId() : null)")
    @Mapping(target = "tableNumber",
            expression = "java(booking.getRestaurantTable() != null ? booking.getRestaurantTable().getTableNumber() : null)")
    @Mapping(target = "resourceType",
            expression = "java(booking.getResourceType().name())")
    @Mapping(target = "status",
            expression = "java(booking.getStatus().name())")
    BookingResponse toResponse(Booking booking);
}