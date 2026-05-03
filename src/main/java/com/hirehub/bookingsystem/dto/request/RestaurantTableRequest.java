package com.hirehub.bookingsystem.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantTableRequest {

    @NotBlank(message = "Table number is required")
    private String tableNumber;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    private String location;
}