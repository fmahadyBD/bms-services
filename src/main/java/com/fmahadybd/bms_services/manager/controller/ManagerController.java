package com.fmahadybd.bms_services.manager.controller;

import com.fmahadybd.bms_services.manager.dto.*;
import com.fmahadybd.bms_services.manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    // ── Create Manager ────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ManagerResponse> createManager(@Valid @RequestBody ManagerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(managerService.createManager(request));
    }

    // ── Get All Managers ──────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    // ── Get Manager by ID ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    // ── Get Manager by Email ──────────────────────────────────────────────
    @GetMapping("/email/{email}")
    public ResponseEntity<ManagerResponse> getManagerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(managerService.getManagerByEmail(email));
    }

    // ── Get Manager by Manager ID ─────────────────────────────────────────
    @GetMapping("/manager-id/{managerId}")
    public ResponseEntity<ManagerResponse> getManagerByManagerId(@PathVariable String managerId) {
        return ResponseEntity.ok(managerService.getManagerByManagerId(managerId));
    }

    // ── Get Manager by Phone ──────────────────────────────────────────────
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<ManagerResponse> getManagerByPhone(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(managerService.getManagerByPhone(phoneNumber));
    }

    // ── Get Managers by Department ────────────────────────────────────────
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ManagerResponse>> getManagersByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(managerService.getManagersByDepartment(department));
    }

    // ── Update Manager ────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponse> updateManager(
            @PathVariable Long id,
            @Valid @RequestBody ManagerUpdateRequest request) {
        return ResponseEntity.ok(managerService.updateManager(id, request));
    }

    // ── Partial Update Manager ────────────────────────────────────────────
    @PatchMapping("/{id}")
    public ResponseEntity<ManagerResponse> partialUpdateManager(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(managerService.partialUpdateManager(id, updates));
    }

    // ── Update Manager Status ─────────────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<ManagerResponse> updateManagerStatus(
            @PathVariable Long id,
            @Valid @RequestBody ManagerStatusUpdateRequest request) {
        return ResponseEntity.ok(managerService.updateManagerStatus(id, request));
    }

    // ── Change Password ───────────────────────────────────────────────────
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ManagerPasswordChangeRequest request,
            @AuthenticationPrincipal Long currentManagerId) {
        managerService.changePassword(id, request, currentManagerId);
        return ResponseEntity.ok().build();
    }

    // ── Delete Manager ────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    // ── Filter Managers ───────────────────────────────────────────────────
    @PostMapping("/filter")
    public ResponseEntity<List<ManagerResponse>> filterManagers(@RequestBody ManagerFilterRequest filter) {
        return ResponseEntity.ok(managerService.filterManagers(filter));
    }

    // ── Get Manager Statistics ────────────────────────────────────────────
    @GetMapping("/statistics")
    public ResponseEntity<ManagerStatistics> getManagerStatistics() {
        return ResponseEntity.ok(managerService.getManagerStatistics());
    }
}