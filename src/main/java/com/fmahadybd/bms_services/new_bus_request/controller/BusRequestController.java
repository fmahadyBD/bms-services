package com.fmahadybd.bms_services.new_bus_request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestRequest;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestResponse;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestStatistics;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestStatusUpdateRequest;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestSummaryResponse;
import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import com.fmahadybd.bms_services.new_bus_request.service.BusRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bus-requests")
@RequiredArgsConstructor
public class BusRequestController {

    private final BusRequestService busRequestService;

    // ── Create Bus Request (Student/Admin) ────────────────────────────────
    @PostMapping
    //@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<BusRequestResponse> createRequest(
            @Valid @RequestBody BusRequestRequest request) {
        // In real app, get userId from SecurityContext
        Long currentUserId = 1L; // Placeholder
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busRequestService.createRequest(request, currentUserId));
    }

    // ── Get All Requests (Admin only) ─────────────────────────────────────
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getAllRequests() {
        return ResponseEntity.ok(busRequestService.getAllRequests());
    }

    // ── Get Request by ID ─────────────────────────────────────────────────
    @GetMapping("/{id}")
   // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<BusRequestResponse> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(busRequestService.getRequestById(id));
    }

    // ── Get Requests by Student ───────────────────────────────────────────
    @GetMapping("/student/{studentId}")
   // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<List<BusRequestSummaryResponse>> getRequestsByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(busRequestService.getRequestsByStudent(studentId));
    }

    // ── Get Requests by Status (Admin only) ───────────────────────────────
    @GetMapping("/status/{status}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getRequestsByStatus(
            @PathVariable BUS_REQUEST_STATUS status) {
        return ResponseEntity.ok(busRequestService.getRequestsByStatus(status));
    }

    // ── Get Pending Requests (Admin only) ─────────────────────────────────
    @GetMapping("/pending")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getPendingRequests() {
        return ResponseEntity.ok(busRequestService.getPendingRequests());
    }

    // ── Get Active Requests ───────────────────────────────────────────────
    @GetMapping("/active")
    //@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getActiveRequests() {
        return ResponseEntity.ok(busRequestService.getActiveRequests());
    }

    // ── Update Request Status (Admin only) ────────────────────────────────
    @PatchMapping("/{id}/status")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusRequestResponse> updateRequestStatus(
            @PathVariable Long id,
            @Valid @RequestBody BusRequestStatusUpdateRequest statusUpdate) {
        // In real app, get adminId from SecurityContext
        Long adminId = 1L; // Placeholder
        return ResponseEntity.ok(busRequestService.updateRequestStatus(id, statusUpdate, adminId));
    }

    // ── Cancel Request (Student) ──────────────────────────────────────────
    @PatchMapping("/{id}/cancel")
   // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<BusRequestResponse> cancelRequest(
            @PathVariable Long id) {
        // In real app, get studentId from SecurityContext
        Long studentId = 1L; // Placeholder
        return ResponseEntity.ok(busRequestService.cancelRequest(id, studentId));
    }

    // ── Delete Request (Admin only) ───────────────────────────────────────
    @DeleteMapping("/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        busRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    // ── Get Requests by Route (Admin only) ────────────────────────────────
    @GetMapping("/route/{routeId}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getRequestsByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(busRequestService.getRequestsByRoute(routeId));
    }

    // ── Get Requests by Pickup Point (Admin only) ─────────────────────────
    @GetMapping("/pickup-point/{pickupPointId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BusRequestResponse>> getRequestsByPickupPoint(
            @PathVariable Long pickupPointId) {
        return ResponseEntity.ok(busRequestService.getRequestsByPickupPoint(pickupPointId));
    }

    // ── Get Statistics (Admin only) ───────────────────────────────────────
    @GetMapping("/statistics")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusRequestStatistics> getStatistics() {
        return ResponseEntity.ok(busRequestService.getStatistics());
    }
}