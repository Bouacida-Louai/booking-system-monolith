package com.hirehub.bookingsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class BookingResponse {
    private Long id;
    private String resourceType;
    private String status;

    // User info
    private Long userId;
    private String userFullName;

    // Hotel fields
    private Long roomId;
    private String roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;

    // Restaurant fields
    private Long tableId;
    private String tableNumber;
    private LocalDate bookingDate;
    private LocalTime timeSlot;
    private Integer durationMinutes;

    private LocalDateTime createdAt;
}