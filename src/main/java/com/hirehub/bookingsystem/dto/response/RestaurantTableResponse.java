package com.hirehub.bookingsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestaurantTableResponse {
    private Long id;
    private String tableNumber;
    private int capacity;
    private String location;
    private boolean available;
}
