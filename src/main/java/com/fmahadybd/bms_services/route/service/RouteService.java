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
import java.util.Map;
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
                .status(ROUTE_STATUS.ACTIVE)  // Default status
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

    // ── Get by Status ─────────────────────────────────────────────────────────
    public List<RouteResponse> findByStatus(ROUTE_STATUS status) {
        return routeRepository.findByStatus(status).stream()
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

    // ── Full Update (PUT) ─────────────────────────────────────────────────────────
    @Transactional
    public RouteResponse update(Long id, UpdateRouteRequest req) {
        Route route = getOrThrow(id);
        
        // Check if bus number is being changed and if it's already taken
        if (!route.getBusNo().equals(req.getBusNo()) && 
            routeRepository.existsByBusNo(req.getBusNo())) {
            throw new DuplicateResourceException("Bus No already exists: " + req.getBusNo());
        }

        // Update basic fields
        route.setBusNo(req.getBusNo());
        route.setRouteName(req.getRouteName());
        route.setRouteLine(req.getRouteLine());
        route.setStatus(req.getStatus());

        // Update pickup points
        updatePickupPoints(route, req.getPickupPoints());

        // Update operating days
        updateOperatingDays(route, req.getOperatingDays());

        return toResponse(routeRepository.save(route));
    }

    // ── Partial Update (PATCH) ───────────────────────────────────────────────────
    @Transactional
    public RouteResponse partialUpdate(Long id, Map<String, Object> updates) {
        Route route = getOrThrow(id);
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "busNo":
                    String newBusNo = (String) value;
                    if (!route.getBusNo().equals(newBusNo) && 
                        routeRepository.existsByBusNo(newBusNo)) {
                        throw new DuplicateResourceException("Bus No already exists: " + newBusNo);
                    }
                    route.setBusNo(newBusNo);
                    break;
                    
                case "routeName":
                    route.setRouteName((String) value);
                    break;
                    
                case "routeLine":
                    route.setRouteLine((String) value);
                    break;
                    
                case "status":
                    route.setStatus(ROUTE_STATUS.valueOf((String) value));
                    break;
                    
                case "operatingDays":
                    @SuppressWarnings("unchecked")
                    List<String> dayStrings = (List<String>) value;
                    List<DAY> days = dayStrings.stream()
                        .map(DAY::valueOf)
                        .collect(Collectors.toList());
                    updateOperatingDays(route, days);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Invalid field for update: " + key);
            }
        });
        
        return toResponse(routeRepository.save(route));
    }

    // ── Update Pickup Points (Helper) ────────────────────────────────────────────
    private void updatePickupPoints(Route route, List<PickupPointRequest> pickupRequests) {
        // Clear existing pickup points
        route.getPickupPoints().clear();
        
        // Create new pickup points
        List<PickupPoint> newPickups = pickupRequests.stream()
            .map(p -> PickupPoint.builder()
                .route(route)
                .placeName(p.getPlaceName())
                .placeDetails(p.getPlaceDetails())
                .pickupTime(LocalTime.parse(p.getPickupTime()))
                .stopOrder(p.getStopOrder())
                .build())
            .collect(Collectors.toList());
        
        route.getPickupPoints().addAll(newPickups);
    }

    // ── Update Operating Days (Helper) ───────────────────────────────────────────
    private void updateOperatingDays(Route route, List<DAY> dayRequests) {
        // Clear existing operating days
        route.getOperatingDays().clear();
        
        // Create new operating days
        List<RouteDay> newDays = dayRequests.stream()
            .map(d -> RouteDay.builder()
                .route(route)
                .day(d)
                .build())
            .collect(Collectors.toList());
        
        route.getOperatingDays().addAll(newDays);
    }

    // ── Add or Update Single Pickup Point ────────────────────────────────────────
    @Transactional
    public PickupPointResponse addPickupPoint(Long routeId, PickupPointRequest req) {
        Route route = getOrThrow(routeId);
        
        // Check if stop order is already taken
        boolean stopOrderExists = route.getPickupPoints().stream()
            .anyMatch(p -> p.getStopOrder().equals(req.getStopOrder()));
        
        if (stopOrderExists) {
            throw new IllegalArgumentException("Stop order " + req.getStopOrder() + " already exists");
        }
        
        PickupPoint pickupPoint = PickupPoint.builder()
            .route(route)
            .placeName(req.getPlaceName())
            .placeDetails(req.getPlaceDetails())
            .pickupTime(LocalTime.parse(req.getPickupTime()))
            .stopOrder(req.getStopOrder())
            .build();
        
        route.getPickupPoints().add(pickupPoint);
        routeRepository.save(route);
        
        return PickupPointResponse.builder()
            .id(pickupPoint.getId())
            .placeName(pickupPoint.getPlaceName())
            .placeDetails(pickupPoint.getPlaceDetails())
            .pickupTime(pickupPoint.getPickupTime())
            .stopOrder(pickupPoint.getStopOrder())
            .build();
    }

    // ── Update Single Pickup Point ───────────────────────────────────────────────
    @Transactional
    public PickupPointResponse updatePickupPoint(Long routeId, Long pickupPointId, PickupPointRequest req) {
        Route route = getOrThrow(routeId);
        
        PickupPoint pickupPoint = route.getPickupPoints().stream()
            .filter(p -> p.getId().equals(pickupPointId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Pickup point not found: " + pickupPointId));
        
        // Check if new stop order conflicts with existing ones (excluding current point)
        boolean stopOrderExists = route.getPickupPoints().stream()
            .filter(p -> !p.getId().equals(pickupPointId))
            .anyMatch(p -> p.getStopOrder().equals(req.getStopOrder()));
        
        if (stopOrderExists) {
            throw new IllegalArgumentException("Stop order " + req.getStopOrder() + " already exists");
        }
        
        pickupPoint.setPlaceName(req.getPlaceName());
        pickupPoint.setPlaceDetails(req.getPlaceDetails());
        pickupPoint.setPickupTime(LocalTime.parse(req.getPickupTime()));
        pickupPoint.setStopOrder(req.getStopOrder());
        
        routeRepository.save(route);
        
        return PickupPointResponse.builder()
            .id(pickupPoint.getId())
            .placeName(pickupPoint.getPlaceName())
            .placeDetails(pickupPoint.getPlaceDetails())
            .pickupTime(pickupPoint.getPickupTime())
            .stopOrder(pickupPoint.getStopOrder())
            .build();
    }

    // ── Delete Single Pickup Point ───────────────────────────────────────────────
    @Transactional
    public void deletePickupPoint(Long routeId, Long pickupPointId) {
        Route route = getOrThrow(routeId);
        
        boolean removed = route.getPickupPoints().removeIf(p -> p.getId().equals(pickupPointId));
        
        if (!removed) {
            throw new ResourceNotFoundException("Pickup point not found: " + pickupPointId);
        }
        
        routeRepository.save(route);
    }

    // ── Reorder Pickup Points ────────────────────────────────────────────────────
    @Transactional
    public List<PickupPointResponse> reorderPickupPoints(Long routeId, List<Long> pickupPointIdsInOrder) {
        Route route = getOrThrow(routeId);
        
        if (pickupPointIdsInOrder.size() != route.getPickupPoints().size()) {
            throw new IllegalArgumentException("Number of pickup points doesn't match");
        }
        
        // Create a map for quick lookup
        Map<Long, PickupPoint> pointMap = route.getPickupPoints().stream()
            .collect(Collectors.toMap(PickupPoint::getId, p -> p));
        
        // Validate all IDs exist
        for (Long id : pickupPointIdsInOrder) {
            if (!pointMap.containsKey(id)) {
                throw new ResourceNotFoundException("Pickup point not found: " + id);
            }
        }
        
        // Update stop orders
        for (int i = 0; i < pickupPointIdsInOrder.size(); i++) {
            PickupPoint point = pointMap.get(pickupPointIdsInOrder.get(i));
            point.setStopOrder(i + 1); // 1-based index
        }
        
        routeRepository.save(route);
        
        // Return reordered list
        return route.getPickupPoints().stream()
            .sorted((p1, p2) -> p1.getStopOrder().compareTo(p2.getStopOrder()))
            .map(p -> PickupPointResponse.builder()
                .id(p.getId())
                .placeName(p.getPlaceName())
                .placeDetails(p.getPlaceDetails())
                .pickupTime(p.getPickupTime())
                .stopOrder(p.getStopOrder())
                .build())
            .collect(Collectors.toList());
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
                .status(r.getStatus())  // Include status
                .pickupPoints(pickups)
                .operatingDays(days)
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}