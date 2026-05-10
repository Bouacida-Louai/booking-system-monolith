package com.hirehub.bookingsystem.service.impl;

import com.hirehub.bookingsystem.dto.request.RestaurantTableRequest;
import com.hirehub.bookingsystem.dto.response.RestaurantTableResponse;
import com.hirehub.bookingsystem.entities.RestaurantTable;
import com.hirehub.bookingsystem.exception.ConflictException;
import com.hirehub.bookingsystem.exception.ResourceNotFoundException;
import com.hirehub.bookingsystem.mappers.RestaurantTableMapper;
import com.hirehub.bookingsystem.repositories.RestaurantTableRepository;
import com.hirehub.bookingsystem.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository tableRepository;
    private final RestaurantTableMapper tableMapper;

    @Override
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public RestaurantTableResponse create(RestaurantTableRequest request) {
        if (tableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new ConflictException(
                    "Table number already exists: " + request.getTableNumber());
        }
        RestaurantTable table = tableMapper.toEntity(request);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public RestaurantTableResponse update(Long id, RestaurantTableRequest request) {
        RestaurantTable table = getTableById(id);
        tableMapper.updateEntity(request, table);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantTableResponse findById(Long id) {
        return tableMapper.toResponse(getTableById(id));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tables", key = "'all'")
    public List<RestaurantTableResponse> findAll() {
        return tableRepository.findAll()
                .stream()
                .map(tableMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "tables", key = "'available'")
    public List<RestaurantTableResponse> findAvailable() {
        return tableRepository.findByAvailableTrue()
                .stream()
                .map(tableMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "tables", allEntries = true)
    public void delete(Long id) {
        RestaurantTable table = getTableById(id);
        tableRepository.delete(table);
    }

    public RestaurantTable getTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Table not found: " + id));
    }
}