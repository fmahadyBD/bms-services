package com.fmahadybd.bms_services.bus.service;

import com.fmahadybd.bms_services.bus.dto.*;
import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.bus.repository.BusRepository;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.RouteResponse;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    // ── Create Bus ────────────────────────────────────────────────────────
    @Transactional
    public BusResponse createBus(BusRequest request, Long managerId) {
        // Check if bus number already exists
        if (busRepository.existsByBusNumber(request.getBusNumber())) {
            throw new DuplicateResourceException("Bus with number " + request.getBusNumber() + " already exists");
        }

        Bus.BusBuilder busBuilder = Bus.builder()
                .busName(request.getBusName())
                .busNumber(request.getBusNumber())
                .status(request.getStatus())
                .driverName(request.getDriverName())
                .helperName(request.getHelperName())
                .driverPhone(request.getDriverPhone())
                .helperPhone(request.getHelperPhone())
                .createdBy(managerId);

        // Assign route if routeId is provided
        if (request.getRouteId() != null) {
            Route route = routeRepository.findById(request.getRouteId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Route not found with id: " + request.getRouteId()));
            busBuilder.route(route);
        }

        Bus savedBus = busRepository.save(busBuilder.build());
        return convertToResponse(savedBus);
    }

    // ── Update Bus ────────────────────────────────────────────────────────
    @Transactional
    public BusResponse updateBus(Long id, BusRequest request, Long managerId) {
        Bus bus = getBusOrThrow(id);

        // Check if bus number is being changed and if it's already taken
        if (!bus.getBusNumber().equals(request.getBusNumber()) &&
                busRepository.existsByBusNumber(request.getBusNumber())) {
            throw new DuplicateResourceException("Bus with number " + request.getBusNumber() + " already exists");
        }

        bus.setBusName(request.getBusName());
        bus.setBusNumber(request.getBusNumber());
        bus.setStatus(request.getStatus());
        bus.setDriverName(request.getDriverName());
        bus.setHelperName(request.getHelperName());
        bus.setDriverPhone(request.getDriverPhone());
        bus.setHelperPhone(request.getHelperPhone());
        bus.setUpdatedBy(managerId);

        // Update route assignment
        if (request.getRouteId() != null) {
            Route route = routeRepository.findById(request.getRouteId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Route not found with id: " + request.getRouteId()));
            bus.setRoute(route);
        } else {
            bus.setRoute(null);
        }

        return convertToResponse(busRepository.save(bus));
    }

    // ── Get All Buses ─────────────────────────────────────────────────────
    public List<BusResponse> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Bus by ID ─────────────────────────────────────────────────────
    public BusResponse getBusById(Long id) {
        return convertToResponse(getBusOrThrow(id));
    }

    // ── Get Bus by Number ─────────────────────────────────────────────────
    public BusResponse getBusByNumber(String busNumber) {
        Bus bus = busRepository.findByBusNumber(busNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with number: " + busNumber));
        return convertToResponse(bus);
    }

    // ── Get Buses by Status ───────────────────────────────────────────────
    public List<BusResponse> getBusesByStatus(BUS_STATUS status) {
        return busRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Buses by Route ────────────────────────────────────────────────
    public List<BusResponse> getBusesByRoute(Long routeId) {
        return busRepository.findByRouteId(routeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Active Buses by Route ─────────────────────────────────────────
    public List<BusResponse> getActiveBusesByRoute(Long routeId) {
        return busRepository.findActiveBusesByRoute(routeId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Available Buses ───────────────────────────────────────────────
    public List<BusResponse> getAvailableBuses() {
        return busRepository.findAvailableBuses().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Search Buses by Driver Name ───────────────────────────────────────
    public List<BusResponse> searchBusesByDriverName(String driverName) {
        return busRepository.findByDriverNameContainingIgnoreCase(driverName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Search Buses by Bus Name ──────────────────────────────────────────
    public List<BusResponse> searchBusesByBusName(String busName) {
        return busRepository.findByBusNameContainingIgnoreCase(busName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Update Bus Status ─────────────────────────────────────────────────
    @Transactional
    public BusResponse updateBusStatus(Long id, BusStatusUpdateRequest statusUpdate, Long managerId) {
        Bus bus = getBusOrThrow(id);
        bus.setStatus(statusUpdate.getStatus());
        bus.setUpdatedBy(managerId);
        return convertToResponse(busRepository.save(bus));
    }

    // ── Assign Bus to Route ───────────────────────────────────────────────
    @Transactional
    public BusResponse assignBusToRoute(Long busId, Long routeId, Long managerId) {
        Bus bus = getBusOrThrow(busId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        bus.setRoute(route);
        bus.setUpdatedBy(managerId);

        return convertToResponse(busRepository.save(bus));
    }

    // ── Remove Bus from Route ─────────────────────────────────────────────
    @Transactional
    public BusResponse removeBusFromRoute(Long busId, Long managerId) {
        Bus bus = getBusOrThrow(busId);
        bus.setRoute(null);
        bus.setUpdatedBy(managerId);
        return convertToResponse(busRepository.save(bus));
    }

    // ── Delete Bus ────────────────────────────────────────────────────────
    @Transactional
    public void deleteBus(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bus not found with id: " + id);
        }
        busRepository.deleteById(id);
    }

    // ── Filter Buses ──────────────────────────────────────────────────────
    public List<BusResponse> filterBuses(BusFilterRequest filter) {
        List<Bus> buses = busRepository.findAll();

        if (filter.getBusName() != null && !filter.getBusName().isEmpty()) {
            buses = buses.stream()
                    .filter(b -> b.getBusName().toLowerCase().contains(filter.getBusName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filter.getBusNumber() != null && !filter.getBusNumber().isEmpty()) {
            buses = buses.stream()
                    .filter(b -> b.getBusNumber().toLowerCase().contains(filter.getBusNumber().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filter.getStatus() != null) {
            buses = buses.stream()
                    .filter(b -> b.getStatus() == filter.getStatus())
                    .collect(Collectors.toList());
        }

        if (filter.getRouteId() != null) {
            buses = buses.stream()
                    .filter(b -> b.getRoute() != null && b.getRoute().getId().equals(filter.getRouteId()))
                    .collect(Collectors.toList());
        }

        if (filter.getDriverName() != null && !filter.getDriverName().isEmpty()) {
            buses = buses.stream()
                    .filter(b -> b.getDriverName().toLowerCase().contains(filter.getDriverName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        return buses.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ── Get Bus Statistics ────────────────────────────────────────────────
    public BusStatistics getBusStatistics() {
        return BusStatistics.builder()
                .totalBuses(busRepository.count())
                .activeBuses(busRepository.countByStatus(BUS_STATUS.ACTIVE))
                .inactiveBuses(busRepository.countByStatus(BUS_STATUS.INACTIVE))
                .maintenanceBuses(busRepository.countByStatus(BUS_STATUS.MAINTENANCE))
                .onTripBuses(busRepository.countByStatus(BUS_STATUS.ON_TRIP))
                .outOfServiceBuses(busRepository.countByStatus(BUS_STATUS.OUT_OF_SERVICE))
                .availableBuses(busRepository.countAvailableBuses())
                .build();
    }

    // ── Get Bus Statistics by Route ───────────────────────────────────────
    public BusStatistics getBusStatisticsByRoute(Long routeId) {
        long totalBuses = busRepository.countByRouteId(routeId);
        long activeBuses = busRepository.countByRouteIdAndStatus(routeId, BUS_STATUS.ACTIVE);
        long onTripBuses = busRepository.countByRouteIdAndStatus(routeId, BUS_STATUS.ON_TRIP);

        return BusStatistics.builder()
                .totalBuses(totalBuses)
                .activeBuses(activeBuses)
                .onTripBuses(onTripBuses)
                .busesByRoute(totalBuses)
                .availableBuses(activeBuses)
                .build();
    }

    // ── Partial Update ────────────────────────────────────────────────────
    @Transactional
    public BusResponse partialUpdateBus(Long id, Map<String, Object> updates, Long managerId) {
        Bus bus = getBusOrThrow(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "busName":
                    bus.setBusName((String) value);
                    break;
                case "busNumber":
                    String newBusNumber = (String) value;
                    if (!bus.getBusNumber().equals(newBusNumber) &&
                            busRepository.existsByBusNumber(newBusNumber)) {
                        throw new DuplicateResourceException("Bus with number " + newBusNumber + " already exists");
                    }
                    bus.setBusNumber(newBusNumber);
                    break;
                case "status":
                    bus.setStatus(BUS_STATUS.valueOf((String) value));
                    break;
                case "driverName":
                    bus.setDriverName((String) value);
                    break;
                case "helperName":
                    bus.setHelperName((String) value);
                    break;
                case "driverPhone":
                    bus.setDriverPhone((String) value);
                    break;
                case "helperPhone":
                    bus.setHelperPhone((String) value);
                    break;
                case "routeId":
                    if (value != null) {
                        Route route = routeRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + value));
                        bus.setRoute(route);
                    } else {
                        bus.setRoute(null);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field for update: " + key);
            }
        });

        bus.setUpdatedBy(managerId);
        return convertToResponse(busRepository.save(bus));
    }

    // ── Helper Methods ────────────────────────────────────────────────────
    private Bus getBusOrThrow(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
    }

    private BusResponse convertToResponse(Bus bus) {
        BusResponse.BusResponseBuilder builder = BusResponse.builder()
                .id(bus.getId())
                .busName(bus.getBusName())
                .busNumber(bus.getBusNumber())
                .status(bus.getStatus())
                .driverName(bus.getDriverName())
                .helperName(bus.getHelperName())
                .driverPhone(bus.getDriverPhone())
                .helperPhone(bus.getHelperPhone())
                .routeId(bus.getRoute() != null ? bus.getRoute().getId() : null) // ✅ fixed
                .createdAt(bus.getCreatedAt())
                .updatedAt(bus.getUpdatedAt())
                .createdBy(bus.getCreatedBy())
                .updatedBy(bus.getUpdatedBy());

        if (bus.getBusSlots() != null && !bus.getBusSlots().isEmpty()) {
            List<BusResponse.BusSlotSummary> slotSummaries = bus.getBusSlots().stream()
                    .map(this::convertToBusSlotSummary)
                    .collect(Collectors.toList());
            builder.busSlots(slotSummaries);
        }

        return builder.build();
    }

    private RouteResponse convertToRouteResponse(Route route) {
        return RouteResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .routeLine(route.getRouteLine())
                .status(route.getStatus())
                .build();
    }

    private BusResponse.BusSlotSummary convertToBusSlotSummary(BusSlot slot) {
        return BusResponse.BusSlotSummary.builder()
                .id(slot.getId())
                .slotName(slot.getSlotName())
                .fromLocation(slot.getFromLocation())
                .toLocation(slot.getToLocation())
                .pickupTime(slot.getPickupTime() != null ? slot.getPickupTime().toString() : null)
                .status(slot.getStatus() != null ? slot.getStatus().toString() : null)
                .build();
    }
}