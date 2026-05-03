package com.hirehub.bookingsystem.repositories;


import com.hirehub.bookingsystem.entities.Booking;
import com.hirehub.bookingsystem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ── User bookings ────────────────────────────────────────────────────────
    List<Booking> findByUserId(Long userId);

    // ── Hotel overlap check ──────────────────────────────────────────────────
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.room.id = :roomId
              AND b.status != :cancelled
              AND b.checkIn  < :checkOut
              AND b.checkOut > :checkIn
            """)
    boolean existsOverlappingHotelBooking(
            @Param("roomId")    Long roomId,
            @Param("checkIn")   LocalDate checkIn,
            @Param("checkOut")  LocalDate checkOut,
            @Param("cancelled") BookingStatus cancelled
    );

    // ── Restaurant overlap check ─────────────────────────────────────────────
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.restaurantTable.id = :tableId
              AND b.status != :cancelled
              AND b.bookingDate = :bookingDate
              AND b.timeSlot < :requestedEnd
              AND FUNCTION('ADDTIME', b.timeSlot,
                    FUNCTION('SEC_TO_TIME', b.durationMinutes * 60)) > :timeSlot
            """)
    boolean existsOverlappingRestaurantBooking(
            @Param("tableId")      Long tableId,
            @Param("bookingDate")  LocalDate bookingDate,
            @Param("timeSlot")     LocalTime timeSlot,
            @Param("requestedEnd") LocalTime requestedEnd,
            @Param("cancelled") BookingStatus cancelled
    );

    // ── Find active bookings for a room ──────────────────────────────────────
    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
              AND b.status != :cancelled
              AND b.checkOut > CURRENT_DATE
            """)
    List<Booking> findActiveHotelBookings(
            @Param("roomId")    Long roomId,
            @Param("cancelled") BookingStatus cancelled
    );

    // ── Find active bookings for a table ─────────────────────────────────────
    @Query("""
            SELECT b FROM Booking b
            WHERE b.restaurantTable.id = :tableId
              AND b.status != :cancelled
              AND b.bookingDate >= CURRENT_DATE
            """)
    List<Booking> findActiveRestaurantBookings(
            @Param("tableId")   Long tableId,
            @Param("cancelled") BookingStatus cancelled
    );
}
