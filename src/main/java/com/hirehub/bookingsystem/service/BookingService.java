package com.hirehub.bookingsystem.service;

import com.hirehub.bookingsystem.dto.request.HotelBookingRequest;
import com.hirehub.bookingsystem.dto.request.RestaurantBookingRequest;
import com.hirehub.bookingsystem.dto.response.BookingResponse;
import com.hirehub.bookingsystem.security.UserPrincipal;

import java.util.List;

public interface BookingService {
    BookingResponse bookRoom(Long userId, HotelBookingRequest request);
    BookingResponse bookTable(Long userId, RestaurantBookingRequest request);
    List<BookingResponse> getUserBookings(Long userId);
    List<BookingResponse> getAllBookings();
    BookingResponse cancelBooking(Long bookingId, UserPrincipal currentUser);

}