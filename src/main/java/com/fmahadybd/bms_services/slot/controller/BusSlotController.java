package com.fmahadybd.bms_services.slot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fmahadybd.bms_services.slot.dto.BusSlotFilterRequest;
import com.fmahadybd.bms_services.slot.dto.BusSlotRequest;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotStatistics;
import com.fmahadybd.bms_services.slot.dto.BusSlotStatusUpdateRequest;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.slot.services.BusSlotService;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bus-slots")
@RequiredArgsConstructor
public class BusSlotController {

    private final BusSlotService busSlotService;

    @PostMapping
    public ResponseEntity<BusSlotResponse> createSlot(
            @Valid @RequestBody BusSlotRequest request) {
        Long managerId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busSlotService.createSlot(request, managerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusSlotResponse> updateSlot(
            @PathVariable Long id,
            @Valid @RequestBody BusSlotRequest request) {
        Long managerId = 1L;
        return ResponseEntity.ok(busSlotService.updateSlot(id, request, managerId));
    }

    @GetMapping
    public ResponseEntity<List<BusSlotResponse>> getAllSlots() {
        return ResponseEntity.ok(busSlotService.getAllSlots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusSlotResponse> getSlotById(@PathVariable Long id) {
        return ResponseEntity.ok(busSlotService.getSlotById(id));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(busSlotService.getSlotsByRoute(routeId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByStatus(
            @PathVariable BUS_SLOT_STATUS status) {
        return ResponseEntity.ok(busSlotService.getSlotsByStatus(status));
    }

    @GetMapping("/time-range")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime) {
        return ResponseEntity.ok(busSlotService.getSlotsByTimeRange(fromTime, toTime));
    }

    @GetMapping("/route/{routeId}/time-range")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByRouteAndTimeRange(
            @PathVariable Long routeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime) {
        return ResponseEntity.ok(busSlotService.getSlotsByRouteAndTimeRange(routeId, fromTime, toTime));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BusSlotResponse> updateSlotStatus(
            @PathVariable Long id,
            @Valid @RequestBody BusSlotStatusUpdateRequest statusUpdate) {
        Long managerId = 1L;
        return ResponseEntity.ok(busSlotService.updateSlotStatus(id, statusUpdate, managerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        busSlotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<BusSlotResponse>> filterSlots(
            @RequestBody BusSlotFilterRequest filter) {
        return ResponseEntity.ok(busSlotService.filterSlots(filter));
    }

    @GetMapping("/statistics")
    public ResponseEntity<BusSlotStatistics> getStatistics() {
        return ResponseEntity.ok(busSlotService.getStatistics());
    }

    @GetMapping("/statistics/route/{routeId}")
    public ResponseEntity<BusSlotStatistics> getStatisticsByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(busSlotService.getStatisticsByRoute(routeId));
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByBus(
            @PathVariable Long busId) {
        return ResponseEntity.ok(busSlotService.getSlotsByBus(busId));
    }

    @GetMapping("/bus/{busId}/time-range")
    public ResponseEntity<List<BusSlotResponse>> getSlotsByBusAndTimeRange(
            @PathVariable Long busId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fromTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime toTime) {
        return ResponseEntity.ok(busSlotService.getSlotsByBusAndTimeRange(busId, fromTime, toTime));
    }

    @GetMapping("/statistics/bus/{busId}")
    public ResponseEntity<BusSlotStatistics> getStatisticsByBus(
            @PathVariable Long busId) {
        return ResponseEntity.ok(busSlotService.getStatisticsByBus(busId));
    }
}