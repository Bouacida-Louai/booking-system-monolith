package com.hirehub.bookingsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String tableNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(length = 50)
    private String location;       // INDOOR, OUTDOOR, TERRACE

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    @OneToMany(mappedBy = "restaurantTable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}