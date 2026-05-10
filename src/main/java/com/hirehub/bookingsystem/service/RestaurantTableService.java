package com.hirehub.bookingsystem.service;



import com.hirehub.bookingsystem.dto.request.RestaurantTableRequest;
import com.hirehub.bookingsystem.dto.response.RestaurantTableResponse;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTableResponse create(RestaurantTableRequest request);
    RestaurantTableResponse update(Long id, RestaurantTableRequest request);
    RestaurantTableResponse findById(Long id);
    List<RestaurantTableResponse> findAll();
    List<RestaurantTableResponse> findAvailable();
    void delete(Long id);
}
