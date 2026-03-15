package com.fmahadybd.bms_services.route.controller;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.route.dto.*;
import com.fmahadybd.bms_services.route.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // ── Create Route ─────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<RouteResponse> create(
            @Valid @RequestBody CreateRouteRequest request) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.create(request, userId));
    }

    // ── Get All Routes ──────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    // ── Get Route by ID ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    // ── Get Route by Bus Number ─────────────────────────────────────────
    @GetMapping("/bus/{busNo}")
    public ResponseEntity<RouteResponse> getByBusNo(@PathVariable String busNo) {
        return ResponseEntity.ok(routeService.findByBusNo(busNo));
    }

    // ── Get Routes by Operating Day ─────────────────────────────────────
    @GetMapping("/day/{day}")
    public ResponseEntity<List<RouteResponse>> getByDay(@PathVariable DAY day) {
        return ResponseEntity.ok(routeService.findByDay(day));
    }

    // ── Get Routes by Status ────────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RouteResponse>> getByStatus(@PathVariable ROUTE_STATUS status) {
        return ResponseEntity.ok(routeService.findByStatus(status));
    }

    // ── Get Routes by Status and Day ────────────────────────────────────
    @GetMapping("/filter")
    public ResponseEntity<List<RouteResponse>> getByStatusAndDay(
            @RequestParam(required = false) ROUTE_STATUS status,
            @RequestParam(required = false) DAY day) {
        if (status != null && day != null) {
            return ResponseEntity.ok(routeService.findByStatusAndDay(status, day));
        } else if (status != null) {
            return ResponseEntity.ok(routeService.findByStatus(status));
        } else if (day != null) {
            return ResponseEntity.ok(routeService.findByDay(day));
        } else {
            return ResponseEntity.ok(routeService.findAll());
        }
    }

    // ── Update Route Status ─────────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<RouteResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ROUTE_STATUS status) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.ok(routeService.updateStatus(id, status, userId));
    }

    // ── Full Update Route (PUT) ─────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRouteRequest request) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.ok(routeService.update(id, request, userId));
    }

    // ── Partial Update Route (PATCH) ────────────────────────────────────
    @PatchMapping("/{id}")
    public ResponseEntity<RouteResponse> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.ok(routeService.partialUpdate(id, updates, userId));
    }

    // ── Delete Route ────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Add Pickup Point ────────────────────────────────────────────────
    @PostMapping("/{routeId}/pickup-points")
    public ResponseEntity<PickupPointResponse> addPickupPoint(
            @PathVariable Long routeId,
            @Valid @RequestBody PickupPointRequest request) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.addPickupPoint(routeId, request, userId));
    }

    // ── Update Pickup Point ─────────────────────────────────────────────
    @PutMapping("/{routeId}/pickup-points/{pickupPointId}")
    public ResponseEntity<PickupPointResponse> updatePickupPoint(
            @PathVariable Long routeId,
            @PathVariable Long pickupPointId,
            @Valid @RequestBody PickupPointRequest request) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.ok(routeService.updatePickupPoint(routeId, pickupPointId, request, userId));
    }

    // ── Delete Pickup Point ─────────────────────────────────────────────
    @DeleteMapping("/{routeId}/pickup-points/{pickupPointId}")
    public ResponseEntity<Void> deletePickupPoint(
            @PathVariable Long routeId,
            @PathVariable Long pickupPointId) {
        Long userId = 1L; // This should come from security context
        routeService.deletePickupPoint(routeId, pickupPointId, userId);
        return ResponseEntity.noContent().build();
    }

    // ── Reorder Pickup Points ───────────────────────────────────────────
    @PatchMapping("/{routeId}/pickup-points/reorder")
    public ResponseEntity<List<PickupPointResponse>> reorderPickupPoints(
            @PathVariable Long routeId,
            @RequestBody List<Long> pickupPointIdsInOrder) {
        Long userId = 1L; // This should come from security context
        return ResponseEntity.ok(routeService.reorderPickupPoints(routeId, pickupPointIdsInOrder, userId));
    }

    // ── Get Route Statistics ────────────────────────────────────────────
    @GetMapping("/{id}/statistics")
    public ResponseEntity<RouteStatistics> getRouteStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteStatistics(id));
    }

    // ── Get All Routes Statistics ───────────────────────────────────────
    @GetMapping("/statistics/all")
    public ResponseEntity<List<RouteStatistics>> getAllRoutesStatistics() {
        return ResponseEntity.ok(routeService.getAllRoutesStatistics());
    }

    // ── Get Buses by Route ──────────────────────────────────────────────
    @GetMapping("/{routeId}/buses")
    public ResponseEntity<?> getBusesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getBusesByRoute(routeId));
    }

    // ── Get Active Buses by Route ───────────────────────────────────────
    @GetMapping("/{routeId}/buses/active")
    public ResponseEntity<?> getActiveBusesByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getActiveBusesByRoute(routeId));
    }

    // ── Get Slots by Route ──────────────────────────────────────────────
    @GetMapping("/{routeId}/slots")
    public ResponseEntity<?> getSlotsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getSlotsByRoute(routeId));
    }

    // ── Get Active Slots by Route ───────────────────────────────────────
    @GetMapping("/{routeId}/slots/active")
    public ResponseEntity<?> getActiveSlotsByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getActiveSlotsByRoute(routeId));
    }
}