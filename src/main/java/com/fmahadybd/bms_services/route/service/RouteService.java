package com.fmahadybd.bms_services.route.service;

import com.fmahadybd.bms_services.bus.dto.BusResponse;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.*;
import com.fmahadybd.bms_services.route.model.*;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.model.BusSlot;
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

        return convertToResponse(routeRepository.save(route));
    }

    // ── Get All ───────────────────────────────────────────────────────────────
    public List<RouteResponse> findAll() {
        return routeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get by ID ─────────────────────────────────────────────────────────────
    public RouteResponse findById(Long id) {
        return convertToResponse(getRouteOrThrow(id));
    }

    // ── Get by Bus No ─────────────────────────────────────────────────────────
    public RouteResponse findByBusNo(String busNo) {
        return convertToResponse(routeRepository.findByBusNo(busNo)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + busNo)));
    }

    // ── Get by Day ────────────────────────────────────────────────────────────
    public List<RouteResponse> findByDay(DAY day) {
        return routeRepository.findByOperatingDays_Day(day).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get by Status ─────────────────────────────────────────────────────────
    public List<RouteResponse> findByStatus(ROUTE_STATUS status) {
        return routeRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Update Status ─────────────────────────────────────────────────────────
    @Transactional
    public RouteResponse updateStatus(Long id, ROUTE_STATUS status) {
        Route route = getRouteOrThrow(id);
        route.setStatus(status);
        return convertToResponse(routeRepository.save(route));
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Transactional
    public void delete(Long id) {
        if (!routeRepository.existsById(id))
            throw new ResourceNotFoundException("Route not found: " + id);
        routeRepository.deleteById(id);
    }

    // ── Full Update (PUT) ─────────────────────────────────────────────────────
    @Transactional
    public RouteResponse update(Long id, UpdateRouteRequest req) {
        Route route = getRouteOrThrow(id);
        
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

        return convertToResponse(routeRepository.save(route));
    }

    // ── Partial Update (PATCH) ─────────────────────────────────────────────────
    @Transactional
    public RouteResponse partialUpdate(Long id, Map<String, Object> updates) {
        Route route = getRouteOrThrow(id);
        
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
        
        return convertToResponse(routeRepository.save(route));
    }

    // ── Update Pickup Points (Helper) ─────────────────────────────────────────
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

    // ── Update Operating Days (Helper) ───────────────────────────────────────
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

    // ── Add Single Pickup Point ──────────────────────────────────────────────
    @Transactional
    public PickupPointResponse addPickupPoint(Long routeId, PickupPointRequest req) {
        Route route = getRouteOrThrow(routeId);
        
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
        
        return convertToPickupPointResponse(pickupPoint);
    }

    // ── Update Single Pickup Point ───────────────────────────────────────────
    @Transactional
    public PickupPointResponse updatePickupPoint(Long routeId, Long pickupPointId, PickupPointRequest req) {
        Route route = getRouteOrThrow(routeId);
        
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
        
        return convertToPickupPointResponse(pickupPoint);
    }

    // ── Delete Single Pickup Point ───────────────────────────────────────────
    @Transactional
    public void deletePickupPoint(Long routeId, Long pickupPointId) {
        Route route = getRouteOrThrow(routeId);
        
        boolean removed = route.getPickupPoints().removeIf(p -> p.getId().equals(pickupPointId));
        
        if (!removed) {
            throw new ResourceNotFoundException("Pickup point not found: " + pickupPointId);
        }
        
        routeRepository.save(route);
    }

    // ── Reorder Pickup Points ────────────────────────────────────────────────
    @Transactional
    public List<PickupPointResponse> reorderPickupPoints(Long routeId, List<Long> pickupPointIdsInOrder) {
        Route route = getRouteOrThrow(routeId);
        
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
            .map(this::convertToPickupPointResponse)
            .collect(Collectors.toList());
    }

    // ── Get Buses by Route ───────────────────────────────────────────────────
    public List<BusResponse> getBusesByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        
        return route.getBuses().stream()
            .map(this::convertToBusResponse)
            .collect(Collectors.toList());
    }

    // ── Get Slots by Route ───────────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        
        return route.getBusSlots().stream()
            .map(this::convertToBusSlotResponse)
            .collect(Collectors.toList());
    }

    // ── Get Active Buses by Route ────────────────────────────────────────────
    public List<BusResponse> getActiveBusesByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        
        return route.getBuses().stream()
            .filter(bus -> bus.getStatus().toString().equals("ACTIVE"))
            .map(this::convertToBusResponse)
            .collect(Collectors.toList());
    }

    // ── Get Active Slots by Route ────────────────────────────────────────────
    public List<BusSlotResponse> getActiveSlotsByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        
        return route.getBusSlots().stream()
            .filter(slot -> slot.getStatus().toString().equals("ACTIVE"))
            .map(this::convertToBusSlotResponse)
            .collect(Collectors.toList());
    }

    // ── Get Route Statistics ─────────────────────────────────────────────────
    public RouteStatistics getRouteStatistics(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        
        long totalBuses = route.getBuses().size();
        long activeBuses = route.getBuses().stream()
            .filter(bus -> bus.getStatus().toString().equals("ACTIVE"))
            .count();
        long totalSlots = route.getBusSlots().size();
        long activeSlots = route.getBusSlots().stream()
            .filter(slot -> slot.getStatus().toString().equals("ACTIVE"))
            .count();
        
        return RouteStatistics.builder()
            .routeId(route.getId())
            .routeName(route.getRouteName())
            .totalBuses(totalBuses)
            .activeBuses(activeBuses)
            .totalSlots(totalSlots)
            .activeSlots(activeSlots)
            .totalPickupPoints(route.getPickupPoints().size())
            .operatingDays(route.getOperatingDays().stream()
                .map(RouteDay::getDay)
                .collect(Collectors.toList()))
            .build();
    }

    // ── Get All Routes Statistics ────────────────────────────────────────────
    public List<RouteStatistics> getAllRoutesStatistics() {
        return routeRepository.findAll().stream()
            .map(route -> getRouteStatistics(route.getId()))
            .collect(Collectors.toList());
    }

    // ── Helper Methods ───────────────────────────────────────────────────────
    private Route getRouteOrThrow(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    private RouteResponse convertToResponse(Route route) {
        List<PickupPointResponse> pickups = route.getPickupPoints().stream()
                .map(this::convertToPickupPointResponse)
                .collect(Collectors.toList());

        List<DAY> days = route.getOperatingDays().stream()
                .map(RouteDay::getDay)
                .collect(Collectors.toList());

        List<BusResponse> buses = route.getBuses() != null ? 
            route.getBuses().stream()
                .map(this::convertToBusResponse)
                .collect(Collectors.toList()) : 
            List.of();

        List<BusSlotResponse> slots = route.getBusSlots() != null ?
            route.getBusSlots().stream()
                .map(this::convertToBusSlotResponse)
                .collect(Collectors.toList()) :
            List.of();

        return RouteResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .routeLine(route.getRouteLine())
                .status(route.getStatus())
                .pickupPoints(pickups)
                .operatingDays(days)
                .buses(buses)
                .busSlots(slots)
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    private PickupPointResponse convertToPickupPointResponse(PickupPoint pickupPoint) {
        return PickupPointResponse.builder()
                .id(pickupPoint.getId())
                .placeName(pickupPoint.getPlaceName())
                .placeDetails(pickupPoint.getPlaceDetails())
                .pickupTime(pickupPoint.getPickupTime())
                .stopOrder(pickupPoint.getStopOrder())
                .build();
    }

    private BusResponse convertToBusResponse(Bus bus) {
        return BusResponse.builder()
                .id(bus.getId())
                .busName(bus.getBusName())
                .busNumber(bus.getBusNumber())
                .status(bus.getStatus())
                .driverName(bus.getDriverName())
                .helperName(bus.getHelperName())
                .driverPhone(bus.getDriverPhone())
                .helperPhone(bus.getHelperPhone())
                .build();
    }

    private BusSlotResponse convertToBusSlotResponse(BusSlot slot) {
        return BusSlotResponse.builder()
                .id(slot.getId())
                .slotName(slot.getSlotName())
                .pickupTime(slot.getPickupTime())
                .dropTime(slot.getDropTime())
                .fromLocation(slot.getFromLocation())
                .toLocation(slot.getToLocation())
                .status(slot.getStatus())
                .description(slot.getDescription())
                .isRegular(slot.isRegular())
                .regularDays(slot.getRegularDays())
                .build();
    }
}