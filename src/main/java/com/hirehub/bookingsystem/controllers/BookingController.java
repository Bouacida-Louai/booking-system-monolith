package com.hirehub.bookingsystem.controllers;

import com.hirehub.bookingsystem.dto.request.HotelBookingRequest;
import com.hirehub.bookingsystem.dto.request.RestaurantBookingRequest;
import com.hirehub.bookingsystem.dto.response.BookingResponse;

import com.hirehub.bookingsystem.security.CurrentUser;
import com.hirehub.bookingsystem.security.UserPrincipal;
import com.hirehub.bookingsystem.service.BookingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management")
@SecurityRequirement(name = "bearerAuth")


public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/hotel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> bookRoom(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody HotelBookingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.bookRoom(currentUser.getId(), request));
    }

    @PostMapping("/restaurant")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> bookTable(
            @CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody RestaurantBookingRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.bookTable(currentUser.getId(), request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(
            @CurrentUser UserPrincipal currentUser) {

        return ResponseEntity.ok(
                bookingService.getUserBookings(currentUser.getId())
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id, currentUser) // ← pass full principal
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}