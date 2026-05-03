package com.hirehub.bookingsystem.repositories;

import com.hirehub.bookingsystem.entities.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByAvailableTrue();

    boolean existsByTableNumber(String tableNumber);
}
