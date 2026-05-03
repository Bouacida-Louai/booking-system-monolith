package com.hirehub.bookingsystem.service;

import com.hirehub.bookingsystem.dto.request.RoomRequest;
import com.hirehub.bookingsystem.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse create(RoomRequest request);
    RoomResponse update(Long id, RoomRequest request);
    RoomResponse findById(Long id);
    List<RoomResponse> findAll();
    List<RoomResponse> findAvailable();
    void delete(Long id);
}
