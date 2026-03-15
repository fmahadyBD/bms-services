package com.fmahadybd.bms_services.route.service;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.bus.dto.BusResponse;
import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.exception.ValidationException;
import com.fmahadybd.bms_services.route.dto.*;
import com.fmahadybd.bms_services.route.model.*;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private final RouteRepository routeRepository;

    // ── Create Route ─────────────────────────────────────────────────────
    @Transactional
    public RouteResponse create(CreateRouteRequest req, Long userId) {
        log.info("Creating new route with bus number: {}", req.getBusNo());

        // Check for duplicate bus number
        if (routeRepository.existsByBusNo(req.getBusNo())) {
            throw new DuplicateResourceException("Bus number already exists: " + req.getBusNo());
        }

        // Validate pickup points order
        validatePickupPointsOrder(req.getPickupPoints());

        // Build route entity
        Route route = Route.builder()
                .busNo(req.getBusNo())
                .routeName(req.getRouteName())
                .routeLine(req.getRouteLine())
                .status(ROUTE_STATUS.ACTIVE)
                .createdBy(userId)
                .build();

        // Add pickup points
        List<PickupPoint> pickupPoints = req.getPickupPoints().stream()
                .map(p -> PickupPoint.builder()
                        .route(route)
                        .placeName(p.getPlaceName())
                        .placeDetails(p.getPlaceDetails())
                        .pickupTime(LocalTime.parse(p.getPickupTime()))
                        .stopOrder(p.getStopOrder())
                        .build())
                .collect(Collectors.toList());
        route.setPickupPoints(pickupPoints);

        // Add operating days
        List<RouteDay> operatingDays = req.getOperatingDays().stream()
                .map(day -> RouteDay.builder()
                        .route(route)
                        .day(day)
                        .build())
                .collect(Collectors.toList());
        route.setOperatingDays(operatingDays);

        Route savedRoute = routeRepository.save(route);
        log.info("Route created successfully with ID: {}", savedRoute.getId());

        return convertToResponse(savedRoute);
    }

    // ── Get All Routes ──────────────────────────────────────────────────
    public List<RouteResponse> findAll() {
        return routeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Route by ID ─────────────────────────────────────────────────
    public RouteResponse findById(Long id) {
        return convertToResponse(getRouteOrThrow(id));
    }

    // ── Get Route by Bus Number ─────────────────────────────────────────
    public RouteResponse findByBusNo(String busNo) {
        return convertToResponse(routeRepository.findByBusNo(busNo)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with bus number: " + busNo)));
    }

    // ── Get Routes by Day ───────────────────────────────────────────────
    public List<RouteResponse> findByDay(DAY day) {
        return routeRepository.findByOperatingDays_Day(day).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Routes by Status ────────────────────────────────────────────
    public List<RouteResponse> findByStatus(ROUTE_STATUS status) {
        return routeRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Routes by Status and Day ────────────────────────────────────
    public List<RouteResponse> findByStatusAndDay(ROUTE_STATUS status, DAY day) {
        return routeRepository.findByStatusAndDay(status, day).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Update Route Status ─────────────────────────────────────────────
    @Transactional
    public RouteResponse updateStatus(Long id, ROUTE_STATUS status, Long userId) {
        Route route = getRouteOrThrow(id);
        route.setStatus(status);
        route.setUpdatedBy(userId);
        return convertToResponse(routeRepository.save(route));
    }

    // ── Full Update Route ───────────────────────────────────────────────
    @Transactional
    public RouteResponse update(Long id, UpdateRouteRequest req, Long userId) {
        log.info("Updating route with ID: {}", id);

        Route route = getRouteOrThrow(id);

        // Check for duplicate bus number if changed
        if (!route.getBusNo().equals(req.getBusNo()) &&
                routeRepository.existsByBusNo(req.getBusNo())) {
            throw new DuplicateResourceException("Bus number already exists: " + req.getBusNo());
        }

        // Validate pickup points order
        validatePickupPointsOrder(req.getPickupPoints());

        // Update basic fields
        route.setBusNo(req.getBusNo());
        route.setRouteName(req.getRouteName());
        route.setRouteLine(req.getRouteLine());
        route.setStatus(req.getStatus());
        route.setUpdatedBy(userId);

        // Update pickup points
        updatePickupPoints(route, req.getPickupPoints());

        // Update operating days
        updateOperatingDays(route, req.getOperatingDays());

        Route updatedRoute = routeRepository.save(route);
        log.info("Route updated successfully with ID: {}", updatedRoute.getId());

        return convertToResponse(updatedRoute);
    }

    // ── Partial Update Route ────────────────────────────────────────────
    @Transactional
    public RouteResponse partialUpdate(Long id, Map<String, Object> updates, Long userId) {
        log.info("Partially updating route with ID: {}", id);

        Route route = getRouteOrThrow(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "busNo":
                    String newBusNo = (String) value;
                    if (!route.getBusNo().equals(newBusNo) &&
                            routeRepository.existsByBusNo(newBusNo)) {
                        throw new DuplicateResourceException("Bus number already exists: " + newBusNo);
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

                default:
                    log.warn("Attempted to update invalid field: {}", key);
                    throw new ValidationException("Invalid field for update: " + key);
            }
        });

        route.setUpdatedBy(userId);
        Route updatedRoute = routeRepository.save(route);
        log.info("Route partially updated successfully with ID: {}", updatedRoute.getId());

        return convertToResponse(updatedRoute);
    }

    // ── Delete Route ────────────────────────────────────────────────────
    @Transactional
    public void delete(Long id) {
        log.info("Deleting route with ID: {}", id);

        Route route = getRouteOrThrow(id);

        // Check if route has associated buses or slots
        if (!route.getBuses().isEmpty()) {
            throw new ValidationException("Cannot delete route with associated buses. Remove buses first.");
        }
        if (!route.getBusSlots().isEmpty()) {
            throw new ValidationException("Cannot delete route with associated slots. Remove slots first.");
        }

        routeRepository.delete(route);
        log.info("Route deleted successfully with ID: {}", id);
    }

    // ── Add Pickup Point ────────────────────────────────────────────────
    @Transactional
    public PickupPointResponse addPickupPoint(Long routeId, PickupPointRequest req, Long userId) {
        log.info("Adding pickup point to route ID: {}", routeId);

        Route route = getRouteOrThrow(routeId);

        // Check for duplicate stop order
        boolean stopOrderExists = route.getPickupPoints().stream()
                .anyMatch(p -> p.getStopOrder().equals(req.getStopOrder()));

        if (stopOrderExists) {
            throw new ValidationException("Stop order " + req.getStopOrder() + " already exists");
        }

        // Check for duplicate time
        boolean timeExists = route.getPickupPoints().stream()
                .anyMatch(p -> p.getPickupTime().equals(LocalTime.parse(req.getPickupTime())));

        if (timeExists) {
            throw new ValidationException("Pickup time " + req.getPickupTime() + " already exists");
        }

        PickupPoint pickupPoint = PickupPoint.builder()
                .route(route)
                .placeName(req.getPlaceName())
                .placeDetails(req.getPlaceDetails())
                .pickupTime(LocalTime.parse(req.getPickupTime()))
                .stopOrder(req.getStopOrder())
                .build();

        route.getPickupPoints().add(pickupPoint);
        route.setUpdatedBy(userId);

        routeRepository.save(route);
        log.info("Pickup point added successfully to route ID: {}", routeId);

        return convertToPickupPointResponse(pickupPoint);
    }

    // ── Update Pickup Point ─────────────────────────────────────────────
    @Transactional
    public PickupPointResponse updatePickupPoint(Long routeId, Long pickupPointId,
                                                 PickupPointRequest req, Long userId) {
        log.info("Updating pickup point ID: {} for route ID: {}", pickupPointId, routeId);

        Route route = getRouteOrThrow(routeId);

        PickupPoint pickupPoint = route.getPickupPoints().stream()
                .filter(p -> p.getId().equals(pickupPointId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pickup point not found with ID: " + pickupPointId));

        // Check for duplicate stop order (excluding current point)
        boolean stopOrderExists = route.getPickupPoints().stream()
                .filter(p -> !p.getId().equals(pickupPointId))
                .anyMatch(p -> p.getStopOrder().equals(req.getStopOrder()));

        if (stopOrderExists) {
            throw new ValidationException("Stop order " + req.getStopOrder() + " already exists");
        }

        // Check for duplicate time (excluding current point)
        boolean timeExists = route.getPickupPoints().stream()
                .filter(p -> !p.getId().equals(pickupPointId))
                .anyMatch(p -> p.getPickupTime().equals(LocalTime.parse(req.getPickupTime())));

        if (timeExists) {
            throw new ValidationException("Pickup time " + req.getPickupTime() + " already exists");
        }

        pickupPoint.setPlaceName(req.getPlaceName());
        pickupPoint.setPlaceDetails(req.getPlaceDetails());
        pickupPoint.setPickupTime(LocalTime.parse(req.getPickupTime()));
        pickupPoint.setStopOrder(req.getStopOrder());

        route.setUpdatedBy(userId);
        routeRepository.save(route);
        log.info("Pickup point updated successfully for route ID: {}", routeId);

        return convertToPickupPointResponse(pickupPoint);
    }

    // ── Delete Pickup Point ─────────────────────────────────────────────
    @Transactional
    public void deletePickupPoint(Long routeId, Long pickupPointId, Long userId) {
        log.info("Deleting pickup point ID: {} from route ID: {}", pickupPointId, routeId);

        Route route = getRouteOrThrow(routeId);

        boolean removed = route.getPickupPoints().removeIf(p -> p.getId().equals(pickupPointId));

        if (!removed) {
            throw new ResourceNotFoundException("Pickup point not found with ID: " + pickupPointId);
        }

        route.setUpdatedBy(userId);
        routeRepository.save(route);
        log.info("Pickup point deleted successfully from route ID: {}", routeId);
    }

    // ── Reorder Pickup Points ───────────────────────────────────────────
    @Transactional
    public List<PickupPointResponse> reorderPickupPoints(Long routeId,
                                                         List<Long> pickupPointIdsInOrder,
                                                         Long userId) {
        log.info("Reordering pickup points for route ID: {}", routeId);

        Route route = getRouteOrThrow(routeId);

        if (pickupPointIdsInOrder.size() != route.getPickupPoints().size()) {
            throw new ValidationException("Number of pickup points doesn't match. Expected: " +
                    route.getPickupPoints().size() + ", Got: " + pickupPointIdsInOrder.size());
        }

        // Create map for quick lookup
        Map<Long, PickupPoint> pointMap = route.getPickupPoints().stream()
                .collect(Collectors.toMap(PickupPoint::getId, p -> p));

        // Validate all IDs exist
        for (Long ppId : pickupPointIdsInOrder) {
            if (!pointMap.containsKey(ppId)) {
                throw new ResourceNotFoundException("Pickup point not found with ID: " + ppId);
            }
        }

        // Update stop orders
        for (int i = 0; i < pickupPointIdsInOrder.size(); i++) {
            PickupPoint point = pointMap.get(pickupPointIdsInOrder.get(i));
            point.setStopOrder(i + 1);
        }

        route.setUpdatedBy(userId);
        routeRepository.save(route);
        log.info("Pickup points reordered successfully for route ID: {}", routeId);

        return route.getPickupPoints().stream()
                .sorted((p1, p2) -> p1.getStopOrder().compareTo(p2.getStopOrder()))
                .map(this::convertToPickupPointResponse)
                .collect(Collectors.toList());
    }

    // ── Get Route Statistics ────────────────────────────────────────────
    public RouteStatistics getRouteStatistics(Long routeId) {
        Route route = getRouteOrThrow(routeId);

        long totalBuses = routeRepository.countBusesByRouteId(routeId);
        long activeBuses = routeRepository.countActiveBusesByRouteId(routeId);
        long totalSlots = routeRepository.countSlotsByRouteId(routeId);
        long activeSlots = routeRepository.countActiveSlotsByRouteId(routeId);

        return RouteStatistics.builder()
                .routeId(route.getId())
                .routeName(route.getRouteName())
                .busNo(route.getBusNo())
                .status(route.getStatus())
                .totalBuses(totalBuses)
                .activeBuses(activeBuses)
                .totalSlots(totalSlots)
                .activeSlots(activeSlots)
                .totalPickupPoints(route.getPickupPoints().size())
                .operatingDays(route.getOperatingDays().stream()
                        .map(RouteDay::getDay)
                        .collect(Collectors.toList()))
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }

    // ── Get All Routes Statistics ───────────────────────────────────────
    public List<RouteStatistics> getAllRoutesStatistics() {
        return routeRepository.findAll().stream()
                .map(route -> getRouteStatistics(route.getId()))
                .collect(Collectors.toList());
    }

    // ── Get Buses by Route ──────────────────────────────────────────────
    public List<BusResponse> getBusesByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        return route.getBuses().stream()
                .map(this::convertToBusResponse)
                .collect(Collectors.toList());
    }

    // ── Get Active Buses by Route ───────────────────────────────────────
    public List<BusResponse> getActiveBusesByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        return route.getBuses().stream()
                .filter(bus -> BUS_STATUS.ACTIVE.equals(bus.getStatus())) // ✅ fixed: proper enum comparison
                .map(this::convertToBusResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Route ──────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        return route.getBusSlots().stream()
                .map(this::convertToBusSlotResponse)
                .collect(Collectors.toList());
    }

    // ── Get Active Slots by Route ───────────────────────────────────────
    public List<BusSlotResponse> getActiveSlotsByRoute(Long routeId) {
        Route route = getRouteOrThrow(routeId);
        return route.getBusSlots().stream()
                .filter(slot -> slot.getStatus() != null &&
                        "ACTIVE".equals(slot.getStatus().name())) // ✅ fixed: enum.name() instead of toString()
                .map(this::convertToBusSlotResponse)
                .collect(Collectors.toList());
    }

    // ── Helper Methods ──────────────────────────────────────────────────
    private Route getRouteOrThrow(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with ID: " + id));
    }

    private void validatePickupPointsOrder(List<PickupPointRequest> pickupPoints) {
        List<Integer> stopOrders = pickupPoints.stream()
                .map(PickupPointRequest::getStopOrder)
                .collect(Collectors.toList());

        // Check for duplicate stop orders
        if (stopOrders.size() != stopOrders.stream().distinct().count()) {
            throw new ValidationException("Duplicate stop orders are not allowed");
        }

        // Check if stop orders start from 1 and are consecutive
        int expectedOrder = 1;
        for (int order : stopOrders.stream().sorted().collect(Collectors.toList())) {
            if (order != expectedOrder) {
                throw new ValidationException("Stop orders must be consecutive starting from 1");
            }
            expectedOrder++;
        }
    }

    private void updatePickupPoints(Route route, List<PickupPointRequest> pickupRequests) {
        route.getPickupPoints().clear();

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

    private void updateOperatingDays(Route route, List<DAY> dayRequests) {
        route.getOperatingDays().clear();

        List<RouteDay> newDays = dayRequests.stream()
                .map(day -> RouteDay.builder()
                        .route(route)
                        .day(day)
                        .build())
                .collect(Collectors.toList());

        route.getOperatingDays().addAll(newDays);
    }

    private RouteResponse convertToResponse(Route route) {
        List<PickupPointResponse> pickupResponses = route.getPickupPoints().stream()
                .map(this::convertToPickupPointResponse)
                .collect(Collectors.toList());

        List<DAY> dayResponses = route.getOperatingDays().stream()
                .map(RouteDay::getDay)
                .collect(Collectors.toList());

        List<BusResponse> busResponses = route.getBuses().stream()
                .map(this::convertToBusResponse)
                .collect(Collectors.toList());

        List<BusSlotResponse> slotResponses = route.getBusSlots().stream()
                .map(this::convertToBusSlotResponse)
                .collect(Collectors.toList());

        return RouteResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .routeLine(route.getRouteLine())
                .status(route.getStatus())
                .pickupPoints(pickupResponses)
                .operatingDays(dayResponses)
                .buses(busResponses)
                .busSlots(slotResponses)
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .createdBy(route.getCreatedBy())
                .updatedBy(route.getUpdatedBy())
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
                .routeId(bus.getRoute() != null ? bus.getRoute().getId() : null)
                .build();
    }

    private BusSlotResponse convertToBusSlotResponse(BusSlot slot) {
        BusSlotResponse.BusSlotResponseBuilder builder = BusSlotResponse.builder()
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
                .createdAt(slot.getCreatedAt())
                .updatedAt(slot.getUpdatedAt())
                .createdBy(slot.getCreatedBy())
                .updatedBy(slot.getUpdatedBy());

        if (slot.getRoute() != null) {
            builder.route(convertToRouteBasicResponse(slot.getRoute()));
        }

        if (slot.getBus() != null) {
            builder.bus(convertToBusBasicResponse(slot.getBus()));
        }

        return builder.build();
    }

    private RouteBasicResponse convertToRouteBasicResponse(Route route) {
        return RouteBasicResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .build();
    }

    private BusBasicResponse convertToBusBasicResponse(Bus bus) {
        return BusBasicResponse.builder()
                .id(bus.getId())
                .busName(bus.getBusName())
                .busNumber(bus.getBusNumber())
                .build();
    }
}