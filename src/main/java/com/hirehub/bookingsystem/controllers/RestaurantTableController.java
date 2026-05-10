package com.hirehub.bookingsystem.controllers;


import com.hirehub.bookingsystem.dto.request.RestaurantTableRequest;
import com.hirehub.bookingsystem.dto.response.RestaurantTableResponse;
import com.hirehub.bookingsystem.service.RestaurantTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    // ── Public ────────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<RestaurantTableResponse>> getAllTables() {
        return ResponseEntity.ok(tableService.findAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTableResponse>> getAvailableTables() {
        return ResponseEntity.ok(tableService.findAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableResponse> getTableById(
            @PathVariable Long id) {
        return ResponseEntity.ok(tableService.findById(id));
    }

    // ── Admin only ────────────────────────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponse> createTable(
            @Valid @RequestBody RestaurantTableRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tableService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableResponse> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantTableRequest request) {

        return ResponseEntity.ok(tableService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
