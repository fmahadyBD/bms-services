package com.fmahadybd.bms_services.route.controller;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.route.dto.*;
import com.fmahadybd.bms_services.route.service.RouteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // ── Create Route ─────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<RouteResponse> create(
            @Valid @RequestBody CreateRouteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.create(req));
    }

    // ── Get All Routes ──────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    // ── Get Route by ID ─────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    // ── Get Route by Bus Number ─────────────────────────────────────────────
    @GetMapping("/bus/{busNo}")
    public ResponseEntity<RouteResponse> getByBusNo(@PathVariable String busNo) {
        return ResponseEntity.ok(routeService.findByBusNo(busNo));
    }

    // ── Get Routes by Operating Day ─────────────────────────────────────────
    @GetMapping("/day/{day}")
    public ResponseEntity<List<RouteResponse>> getByDay(@PathVariable DAY day) {
        return ResponseEntity.ok(routeService.findByDay(day));
    }

    // ── Get Routes by Status ────────────────────────────────────────────────
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RouteResponse>> getByStatus(@PathVariable ROUTE_STATUS status) {
        return ResponseEntity.ok(routeService.findByStatus(status));
    }

    // ── Update Route Status ─────────────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<RouteResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ROUTE_STATUS status) {
        return ResponseEntity.ok(routeService.updateStatus(id, status));
    }

    // ── Delete Route ────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Full Update Route (PUT) ─────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRouteRequest req) {
        return ResponseEntity.ok(routeService.update(id, req));
    }

    // ── Partial Update Route (PATCH) ────────────────────────────────────────
    @PatchMapping("/{id}")
    public ResponseEntity<RouteResponse> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(routeService.partialUpdate(id, updates));
    }

    // ── Add Pickup Point to Route ───────────────────────────────────────────
    @PostMapping("/{routeId}/pickup-points")
    public ResponseEntity<PickupPointResponse> addPickupPoint(
            @PathVariable Long routeId,
            @Valid @RequestBody PickupPointRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.addPickupPoint(routeId, req));
    }

    // ── Update Pickup Point ─────────────────────────────────────────────────
    @PutMapping("/{routeId}/pickup-points/{pickupPointId}")
    public ResponseEntity<PickupPointResponse> updatePickupPoint(
            @PathVariable Long routeId,
            @PathVariable Long pickupPointId,
            @Valid @RequestBody PickupPointRequest req) {
        return ResponseEntity.ok(routeService.updatePickupPoint(routeId, pickupPointId, req));
    }

    // ── Delete Pickup Point ─────────────────────────────────────────────────
    @DeleteMapping("/{routeId}/pickup-points/{pickupPointId}")
    public ResponseEntity<Void> deletePickupPoint(
            @PathVariable Long routeId,
            @PathVariable Long pickupPointId) {
        routeService.deletePickupPoint(routeId, pickupPointId);
        return ResponseEntity.noContent().build();
    }

    // ── Reorder Pickup Points ───────────────────────────────────────────────
    @PatchMapping("/{routeId}/pickup-points/reorder")
    public ResponseEntity<List<PickupPointResponse>> reorderPickupPoints(
            @PathVariable Long routeId,
            @RequestBody List<Long> pickupPointIdsInOrder) {
        return ResponseEntity.ok(routeService.reorderPickupPoints(routeId, pickupPointIdsInOrder));
    }
}