package com.fmahadybd.bms_services.bus.controller;

import com.fmahadybd.bms_services.bus.dto.*;
import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.bus.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    // ── Create Bus ─────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<BusResponse> createBus(@Valid @RequestBody BusRequest request) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busService.createBus(request, managerId));
    }

    // ── Get All Buses ─────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    // ── Get Bus by ID ─────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<BusResponse> getBusById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    // ── Get Bus by Number ─────────────────────────────────────────────────
    @GetMapping("/number/{busNumber}")
    public ResponseEntity<BusResponse> getBusByNumber(@PathVariable String busNumber) {
        return ResponseEntity.ok(busService.getBusByNumber(busNumber));
    }

    // ── Get Buses by Status ───────────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BusResponse>> getBusesByStatus(@PathVariable BUS_STATUS status) {
        return ResponseEntity.ok(busService.getBusesByStatus(status));
    }

    // ── Get Buses by Route ────────────────────────────────────────────────
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<BusResponse>> getBusesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(busService.getBusesByRoute(routeId));
    }

    // ── Get Active Buses by Route ─────────────────────────────────────────
    @GetMapping("/route/{routeId}/active")
    public ResponseEntity<List<BusResponse>> getActiveBusesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(busService.getActiveBusesByRoute(routeId));
    }

    // ── Get Available Buses ───────────────────────────────────────────────
    @GetMapping("/available")
    public ResponseEntity<List<BusResponse>> getAvailableBuses() {
        return ResponseEntity.ok(busService.getAvailableBuses());
    }

    // ── Search Buses by Driver Name ───────────────────────────────────────
    @GetMapping("/search/driver")
    public ResponseEntity<List<BusResponse>> searchBusesByDriverName(
            @RequestParam String driverName) {
        return ResponseEntity.ok(busService.searchBusesByDriverName(driverName));
    }

    // ── Search Buses by Bus Name ──────────────────────────────────────────
    @GetMapping("/search/name")
    public ResponseEntity<List<BusResponse>> searchBusesByBusName(
            @RequestParam String busName) {
        return ResponseEntity.ok(busService.searchBusesByBusName(busName));
    }

    // ── Update Bus ────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusRequest request) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(busService.updateBus(id, request, managerId));
    }

    // ── Partial Update Bus ────────────────────────────────────────────────
    @PatchMapping("/{id}")
    public ResponseEntity<BusResponse> partialUpdateBus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(busService.partialUpdateBus(id, updates, managerId));
    }

    // ── Update Bus Status ─────────────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<BusResponse> updateBusStatus(
            @PathVariable Long id,
            @Valid @RequestBody BusStatusUpdateRequest statusUpdate) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(busService.updateBusStatus(id, statusUpdate, managerId));
    }

    // ── Assign Bus to Route ───────────────────────────────────────────────
    @PatchMapping("/{busId}/assign-route/{routeId}")
    public ResponseEntity<BusResponse> assignBusToRoute(
            @PathVariable Long busId,
            @PathVariable Long routeId) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(busService.assignBusToRoute(busId, routeId, managerId));
    }

    // ── Remove Bus from Route ─────────────────────────────────────────────
    @PatchMapping("/{busId}/remove-route")
    public ResponseEntity<BusResponse> removeBusFromRoute(@PathVariable Long busId) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(busService.removeBusFromRoute(busId, managerId));
    }

    // ── Filter Buses ──────────────────────────────────────────────────────
    @PostMapping("/filter")
    public ResponseEntity<List<BusResponse>> filterBuses(@RequestBody BusFilterRequest filter) {
        return ResponseEntity.ok(busService.filterBuses(filter));
    }

    // ── Get Bus Statistics ────────────────────────────────────────────────
    @GetMapping("/statistics")
    public ResponseEntity<BusStatistics> getBusStatistics() {
        return ResponseEntity.ok(busService.getBusStatistics());
    }

    // ── Get Bus Statistics by Route ───────────────────────────────────────
    @GetMapping("/statistics/route/{routeId}")
    public ResponseEntity<BusStatistics> getBusStatisticsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(busService.getBusStatisticsByRoute(routeId));
    }

    // ── Delete Bus ────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }
}