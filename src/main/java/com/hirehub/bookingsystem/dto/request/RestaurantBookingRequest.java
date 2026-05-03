package com.hirehub.bookingsystem.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RestaurantBookingRequest {

    @NotNull(message = "Table ID is required")
    private Long tableId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate bookingDate;

    @NotNull(message = "Time slot is required")
    private LocalTime timeSlot;

    @NotNull(message = "Duration is required")
    @Min(value = 30, message = "Minimum duration is 30 minutes")
    private Integer durationMinutes;
}