package com.hirehub.bookingsystem.entities;

import com.hirehub.bookingsystem.enums.BookingStatus;
import com.hirehub.bookingsystem.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Who booked ──────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ── What was booked (one will be null) ──────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_table_id")
    private RestaurantTable restaurantTable;

    // ── Resource discriminator ───────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceType resourceType;

    // ── Hotel fields (null for restaurant bookings) ──────────
    private LocalDate checkIn;
    private LocalDate checkOut;

    // ── Restaurant fields (null for hotel bookings) ──────────
    private LocalDate bookingDate;
    private LocalTime timeSlot;       // e.g. 19:00
    private Integer durationMinutes;  // e.g. 90

    // ── Common fields ────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}