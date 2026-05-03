package com.hirehub.bookingsystem.service.impl;

import com.hirehub.bookingsystem.dto.request.RoomRequest;
import com.hirehub.bookingsystem.dto.response.RoomResponse;
import com.hirehub.bookingsystem.entities.Room;
import com.hirehub.bookingsystem.mappers.RoomMapper;
import com.hirehub.bookingsystem.repositories.RoomRepository;
import com.hirehub.bookingsystem.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    @CacheEvict(value = "rooms", allEntries = true)
    public RoomResponse create(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new ConflictException("Room number already exists: " + request.getRoomNumber());
        }
        Room room = roomMapper.toEntity(request);
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    @Transactional
    @CacheEvict(value = "rooms", allEntries = true)
    public RoomResponse update(Long id, RoomRequest request) {
        Room room = getRoomById(id);
        roomMapper.updateEntity(request, room);
        return roomMapper.toResponse(roomRepository.save(room));
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse findById(Long id) {
        return roomMapper.toResponse(getRoomById(id));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "rooms", key = "'all'")
    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "rooms", key = "'available'")
    public List<RoomResponse> findAvailable() {
        return roomRepository.findByAvailableTrue()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "rooms", allEntries = true)
    public void delete(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    // Internal helper — reused by BookingService
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }



}
