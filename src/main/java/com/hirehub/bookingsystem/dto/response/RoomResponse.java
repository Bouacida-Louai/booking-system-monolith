package com.hirehub.bookingsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String type;
    private int capacity;
    private BigDecimal pricePerNight;
    private boolean available;
}
