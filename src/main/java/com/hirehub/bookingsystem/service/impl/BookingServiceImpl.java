package com.hirehub.bookingsystem.service.impl;

import com.hirehub.bookingsystem.dto.request.HotelBookingRequest;
import com.hirehub.bookingsystem.dto.request.RestaurantBookingRequest;
import com.hirehub.bookingsystem.dto.response.BookingResponse;
import com.hirehub.bookingsystem.entities.Booking;
import com.hirehub.bookingsystem.entities.RestaurantTable;
import com.hirehub.bookingsystem.entities.Room;
import com.hirehub.bookingsystem.entities.User;
import com.hirehub.bookingsystem.enums.BookingStatus;
import com.hirehub.bookingsystem.enums.ResourceType;
import com.hirehub.bookingsystem.exception.BookingConflictException;
import com.hirehub.bookingsystem.exception.BusinessException;
import com.hirehub.bookingsystem.exception.ResourceNotFoundException;
import com.hirehub.bookingsystem.mappers.BookingMapper;
import com.hirehub.bookingsystem.repositories.BookingRepository;
import com.hirehub.bookingsystem.repositories.RestaurantTableRepository;
import com.hirehub.bookingsystem.repositories.RoomRepository;
import com.hirehub.bookingsystem.repositories.UserRepository;
import com.hirehub.bookingsystem.security.UserPrincipal;
import com.hirehub.bookingsystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    // ── Hotel Booking ────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BookingResponse bookRoom(Long userId, HotelBookingRequest request) {

        // 1. Validate date range
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new BusinessException("Check-out must be after check-in");
        }

        // 2. Load user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // 3. Lock the room row — prevents concurrent bookings
        Room room = roomRepository.findByIdWithLock(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + request.getRoomId()));

        // 4. Check availability flag
        if (!room.isAvailable()) {
            throw new BusinessException("Room " + room.getRoomNumber() + " is not available");
        }

        // 5. Check for overlapping bookings
        boolean overlap = bookingRepository.existsOverlappingHotelBooking(
                room.getId(),
                request.getCheckIn(),
                request.getCheckOut(),
                BookingStatus.CANCELLED
        );

        if (overlap) {
            throw new BookingConflictException(
                    "Room " + room.getRoomNumber() +
                            " is already booked for the selected dates"
            );
        }

        // 6. Create and save booking
        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .resourceType(ResourceType.HOTEL)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .status(BookingStatus.CONFIRMED)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // ── Restaurant Booking ───────────────────────────────────────────────────

    @Override
    @Transactional
    public BookingResponse bookTable(Long userId, RestaurantBookingRequest request) {

        // 1. Load user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // 2. Lock the table row
        RestaurantTable table = tableRepository.findByIdWithLock(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found: " + request.getTableId()));

        // 3. Check availability flag
        if (!table.isAvailable()) {
            throw new BusinessException("Table " + table.getTableNumber() + " is not available");
        }

        // 4. Compute requested end time
        LocalTime requestedEnd = request.getTimeSlot()
                .plusMinutes(request.getDurationMinutes());

        // 5. Check for overlapping bookings
        boolean overlap = bookingRepository.existsOverlappingRestaurantBooking(
                table.getId(),
                request.getBookingDate(),
                request.getTimeSlot(),
                requestedEnd,
                BookingStatus.CANCELLED
        );

        if (overlap) {
            throw new BookingConflictException(
                    "Table " + table.getTableNumber() +
                            " is already booked for the selected time slot"
            );
        }

        // 6. Create and save booking
        Booking booking = Booking.builder()
                .user(user)
                .restaurantTable(table)
                .resourceType(ResourceType.RESTAURANT)
                .bookingDate(request.getBookingDate())
                .timeSlot(request.getTimeSlot())
                .durationMinutes(request.getDurationMinutes())
                .status(BookingStatus.CONFIRMED)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // ── Cancel Booking ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, UserPrincipal currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        // Admin can cancel any booking — customer can only cancel their own
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !booking.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }
    // ── Queries ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}