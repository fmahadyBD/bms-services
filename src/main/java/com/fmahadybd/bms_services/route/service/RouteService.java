package com.fmahadybd.bms_services.route.service;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.*;
import com.fmahadybd.bms_services.route.model.*;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    // ── Create ────────────────────────────────────────────────────────────────
    @Transactional
    public RouteResponse create(CreateRouteRequest req) {
        if (routeRepository.existsByBusNo(req.getBusNo()))
            throw new DuplicateResourceException("Bus No already exists: " + req.getBusNo());

        Route route = Route.builder()
                .busNo(req.getBusNo())
                .routeName(req.getRouteName())
                .routeLine(req.getRouteLine())
                .status(ROUTE_STATUS.ACTIVE)
                .build();

        // Map pickup points
        List<PickupPoint> pickups = req.getPickupPoints().stream()
                .map(p -> PickupPoint.builder()
                        .route(route)
                        .placeName(p.getPlaceName())
                        .placeDetails(p.getPlaceDetails())
                        .pickupTime(LocalTime.parse(p.getPickupTime()))
                        .stopOrder(p.getStopOrder())
                        .build())
                .collect(Collectors.toList());

        // Map operating days
        List<RouteDay> days = req.getOperatingDays().stream()
                .map(d -> RouteDay.builder()
                        .route(route)
                        .day(d)
                        .build())
                .collect(Collectors.toList());

        route.setPickupPoints(pickups);
        route.setOperatingDays(days);

        return toResponse(routeRepository.save(route));
    }

    // ── Get All ───────────────────────────────────────────────────────────────
    public List<RouteResponse> findAll() {
        return routeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get by ID ─────────────────────────────────────────────────────────────
    public RouteResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    // ── Get by Bus No ─────────────────────────────────────────────────────────
    public RouteResponse findByBusNo(String busNo) {
        return toResponse(routeRepository.findByBusNo(busNo)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + busNo)));
    }

    // ── Get by Day ────────────────────────────────────────────────────────────
    public List<RouteResponse> findByDay(DAY day) {
        return routeRepository.findByOperatingDays_Day(day).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update Status ─────────────────────────────────────────────────────────
    @Transactional
    public RouteResponse updateStatus(Long id, ROUTE_STATUS status) {
        Route route = getOrThrow(id);
        route.setStatus(status);
        return toResponse(routeRepository.save(route));
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Transactional
    public void delete(Long id) {
        if (!routeRepository.existsById(id))
            throw new ResourceNotFoundException("Route not found: " + id);
        routeRepository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Route getOrThrow(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    private RouteResponse toResponse(Route r) {
        List<PickupPointResponse> pickups = r.getPickupPoints().stream()
                .map(p -> PickupPointResponse.builder()
                        .id(p.getId())
                        .placeName(p.getPlaceName())
                        .placeDetails(p.getPlaceDetails())
                        .pickupTime(p.getPickupTime())
                        .stopOrder(p.getStopOrder())
                        .build())
                .collect(Collectors.toList());

        List<DAY> days = r.getOperatingDays().stream()
                .map(RouteDay::getDay)
                .collect(Collectors.toList());

        return RouteResponse.builder()
                .id(r.getId())
                .busNo(r.getBusNo())
                .routeName(r.getRouteName())
                .routeLine(r.getRouteLine())
                .status(r.getStatus())
                .pickupPoints(pickups)
                .operatingDays(days)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}