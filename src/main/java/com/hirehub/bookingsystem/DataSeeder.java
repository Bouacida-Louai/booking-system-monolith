package com.hirehub.bookingsystem;


import com.hirehub.bookingsystem.entities.Booking;
import com.hirehub.bookingsystem.entities.RestaurantTable;
import com.hirehub.bookingsystem.entities.Room;
import com.hirehub.bookingsystem.entities.User;
import com.hirehub.bookingsystem.enums.BookingStatus;
import com.hirehub.bookingsystem.enums.ResourceType;
import com.hirehub.bookingsystem.enums.Role;
import com.hirehub.bookingsystem.repositories.BookingRepository;
import com.hirehub.bookingsystem.repositories.RestaurantTableRepository;
import com.hirehub.bookingsystem.repositories.RoomRepository;
import com.hirehub.bookingsystem.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RestaurantTableRepository tableRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder          passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        // Run only if DB is empty
        if (userRepository.count() > 0) {
            log.info("⏭️  Database already seeded — skipping.");
            return;
        }

        log.info("🌱 Seeding database...");

        // ── Users ─────────────────────────────────────────────────────────────
        User admin = userRepository.save(User.builder()
                .firstName("Super")
                .lastName("Admin")
                .email("admin@booking.com")
                .password(passwordEncoder.encode("admin1234"))
                .role(Role.ADMIN)
                .enabled(true)
                .build());

        User customer1 = userRepository.save(User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@booking.com")
                .password(passwordEncoder.encode("john1234"))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build());

        User customer2 = userRepository.save(User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@booking.com")
                .password(passwordEncoder.encode("jane1234"))
                .role(Role.CUSTOMER)
                .enabled(true)
                .build());

        log.info("✅ Users created: admin, john, jane");

        // ── Rooms ─────────────────────────────────────────────────────────────
        Room room101 = roomRepository.save(Room.builder()
                .roomNumber("101")
                .type("SINGLE")
                .capacity(1)
                .pricePerNight(new BigDecimal("59.99"))
                .available(true)
                .build());

        Room room102 = roomRepository.save(Room.builder()
                .roomNumber("102")
                .type("DOUBLE")
                .capacity(2)
                .pricePerNight(new BigDecimal("99.99"))
                .available(true)
                .build());

        Room room201 = roomRepository.save(Room.builder()
                .roomNumber("201")
                .type("SUITE")
                .capacity(4)
                .pricePerNight(new BigDecimal("199.99"))
                .available(true)
                .build());

        Room room202 = roomRepository.save(Room.builder()
                .roomNumber("202")
                .type("DOUBLE")
                .capacity(2)
                .pricePerNight(new BigDecimal("109.99"))
                .available(true)
                .build());

        log.info("✅ Rooms created: 101 (Single), 102 (Double), 201 (Suite), 202 (Double)");

        // ── Restaurant Tables ─────────────────────────────────────────────────
        RestaurantTable table1 = tableRepository.save(RestaurantTable.builder()
                .tableNumber("T1")
                .capacity(2)
                .location("INDOOR")
                .available(true)
                .build());

        RestaurantTable table2 = tableRepository.save(RestaurantTable.builder()
                .tableNumber("T2")
                .capacity(4)
                .location("OUTDOOR")
                .available(true)
                .build());

        RestaurantTable table3 = tableRepository.save(RestaurantTable.builder()
                .tableNumber("T3")
                .capacity(6)
                .location("TERRACE")
                .available(true)
                .build());

        log.info("✅ Tables created: T1 (Indoor/2), T2 (Outdoor/4), T3 (Terrace/6)");

        // ── Hotel Bookings ────────────────────────────────────────────────────

        // Confirmed booking — john books room 101
        bookingRepository.save(Booking.builder()
                .user(customer1)
                .room(room101)
                .resourceType(ResourceType.HOTEL)
                .checkIn(LocalDate.now().plusDays(5))
                .checkOut(LocalDate.now().plusDays(8))
                .status(BookingStatus.CONFIRMED)
                .build());

        // Another booking — jane books room 102
        bookingRepository.save(Booking.builder()
                .user(customer2)
                .room(room102)
                .resourceType(ResourceType.HOTEL)
                .checkIn(LocalDate.now().plusDays(2))
                .checkOut(LocalDate.now().plusDays(4))
                .status(BookingStatus.CONFIRMED)
                .build());

        // Cancelled booking — john had room 201 but cancelled
        bookingRepository.save(Booking.builder()
                .user(customer1)
                .room(room201)
                .resourceType(ResourceType.HOTEL)
                .checkIn(LocalDate.now().plusDays(10))
                .checkOut(LocalDate.now().plusDays(12))
                .status(BookingStatus.CANCELLED)
                .build());

        log.info("✅ Hotel bookings created");

        // ── Restaurant Bookings ───────────────────────────────────────────────

        // John books table T1 tonight at 19:00 for 90 min
        bookingRepository.save(Booking.builder()
                .user(customer1)
                .restaurantTable(table1)
                .resourceType(ResourceType.RESTAURANT)
                .bookingDate(LocalDate.now().plusDays(1))
                .timeSlot(LocalTime.of(19, 0))
                .durationMinutes(90)
                .status(BookingStatus.CONFIRMED)
                .build());

        // Jane books table T2 same day different time
        bookingRepository.save(Booking.builder()
                .user(customer2)
                .restaurantTable(table2)
                .resourceType(ResourceType.RESTAURANT)
                .bookingDate(LocalDate.now().plusDays(1))
                .timeSlot(LocalTime.of(20, 30))
                .durationMinutes(60)
                .status(BookingStatus.CONFIRMED)
                .build());

        log.info("✅ Restaurant bookings created");
        log.info("🎉 Database seeding complete!");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("👤 admin@booking.com   / admin1234  [ADMIN]");
        log.info("👤 john@booking.com    / john1234   [CUSTOMER]");
        log.info("👤 jane@booking.com    / jane1234   [CUSTOMER]");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
