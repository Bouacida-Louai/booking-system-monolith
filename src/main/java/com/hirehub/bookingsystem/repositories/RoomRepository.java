package com.hirehub.bookingsystem.repositories;



import com.hirehub.bookingsystem.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    boolean existsByRoomNumber(String roomNumber);
}
