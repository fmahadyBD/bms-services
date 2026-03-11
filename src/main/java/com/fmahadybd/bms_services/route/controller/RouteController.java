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

@RestController
@RequestMapping("/api/v1/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponse> create(
            @Valid @RequestBody CreateRouteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @GetMapping("/bus/{busNo}")
    public ResponseEntity<RouteResponse> getByBusNo(@PathVariable String busNo) {
        return ResponseEntity.ok(routeService.findByBusNo(busNo));
    }

    @GetMapping("/day/{day}")
    public ResponseEntity<List<RouteResponse>> getByDay(@PathVariable DAY day) {
        return ResponseEntity.ok(routeService.findByDay(day));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RouteResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ROUTE_STATUS status) {
        return ResponseEntity.ok(routeService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}