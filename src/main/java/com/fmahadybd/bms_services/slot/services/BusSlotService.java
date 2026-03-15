package com.fmahadybd.bms_services.slot.services;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.bus.repository.BusRepository;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.dto.BusSlotFilterRequest;
import com.fmahadybd.bms_services.slot.dto.BusSlotRequest;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotStatistics;
import com.fmahadybd.bms_services.slot.dto.BusSlotStatusUpdateRequest;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.slot.repository.BusSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusSlotService {

    private final BusSlotRepository busSlotRepository;
    private final BusRepository busRepository;

    // ── Create Bus Slot ────────────────────────────────────────────────────
    @Transactional
    public BusSlotResponse createSlot(BusSlotRequest request, Long managerId) {
        boolean slotExists = busSlotRepository.existsByRouteIdAndPickupTime(
            request.getRouteId(),
            request.getPickupTime()
        );

        if (slotExists) {
            throw new DuplicateResourceException(
                "Slot already exists for this route at " + request.getPickupTime()
            );
        }

        Route route = Route.builder().id(request.getRouteId()).build();

        BusSlot.BusSlotBuilder slotBuilder = BusSlot.builder()
                .route(route)
                .slotName(request.getSlotName())
                .pickupTime(request.getPickupTime())
                .dropTime(request.getDropTime())
                .fromLocation(request.getFromLocation())
                .toLocation(request.getToLocation())
                .status(request.getStatus() != null ? request.getStatus() : BUS_SLOT_STATUS.ACTIVE)
                .description(request.getDescription())
                .isRegular(request.isRegular())
                .regularDays(request.getRegularDays())
                .createdBy(managerId);

        if (request.getBusId() != null) {
            Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Bus not found with id: " + request.getBusId()));
            slotBuilder.bus(bus);
        }

        BusSlot savedSlot = busSlotRepository.save(slotBuilder.build());
        return toResponse(savedSlot);
    }

    // ── Update Bus Slot ────────────────────────────────────────────────────
    @Transactional
    public BusSlotResponse updateSlot(Long id, BusSlotRequest request, Long managerId) {
        BusSlot slot = getOrThrow(id);

        if (!slot.getPickupTime().equals(request.getPickupTime())) {
            boolean slotExists = busSlotRepository.existsByRouteIdAndPickupTime(
                request.getRouteId(),
                request.getPickupTime()
            );
            if (slotExists) {
                throw new DuplicateResourceException(
                    "Another slot exists for this route at " + request.getPickupTime()
                );
            }
        }

        slot.setSlotName(request.getSlotName());
        slot.setPickupTime(request.getPickupTime());
        slot.setDropTime(request.getDropTime());
        slot.setFromLocation(request.getFromLocation());
        slot.setToLocation(request.getToLocation());
        slot.setDescription(request.getDescription());
        slot.setRegular(request.isRegular());
        slot.setRegularDays(request.getRegularDays());
        slot.setUpdatedBy(managerId);

        if (request.getBusId() != null) {
            Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Bus not found with id: " + request.getBusId()));
            slot.setBus(bus);
        } else {
            slot.setBus(null);
        }

        if (request.getStatus() != null) {
            slot.setStatus(request.getStatus());
        }

        return toResponse(busSlotRepository.save(slot));
    }

    // ── Get All Slots ──────────────────────────────────────────────────────
    public List<BusSlotResponse> getAllSlots() {
        return busSlotRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slot by ID ─────────────────────────────────────────────────────
    public BusSlotResponse getSlotById(Long id) {
        return toResponse(getOrThrow(id));
    }

    // ── Get Slots by Route ─────────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByRoute(Long routeId) {
        return busSlotRepository.findByRouteId(routeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Bus ───────────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByBus(Long busId) {
        return busSlotRepository.findByBusId(busId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Status ────────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByStatus(BUS_SLOT_STATUS status) {
        return busSlotRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Time Range ────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByTimeRange(LocalTime fromTime, LocalTime toTime) {
        return busSlotRepository.findByPickupTimeBetween(fromTime, toTime).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Route and Time ────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByRouteAndTimeRange(
            Long routeId, LocalTime fromTime, LocalTime toTime) {
        return busSlotRepository.findByRouteIdAndPickupTimeBetween(routeId, fromTime, toTime).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Slots by Bus and Time ──────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByBusAndTimeRange(
            Long busId, LocalTime fromTime, LocalTime toTime) {
        return busSlotRepository.findByBusIdAndPickupTimeBetween(busId, fromTime, toTime).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update Slot Status ─────────────────────────────────────────────────
    @Transactional
    public BusSlotResponse updateSlotStatus(Long id, BusSlotStatusUpdateRequest statusUpdate, Long managerId) {
        BusSlot slot = getOrThrow(id);
        slot.setStatus(statusUpdate.getStatus());
        slot.setUpdatedBy(managerId);
        return toResponse(busSlotRepository.save(slot));
    }

    // ── Delete Slot ────────────────────────────────────────────────────────
    @Transactional
    public void deleteSlot(Long id) {
        if (!busSlotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bus slot not found with id: " + id);
        }
        busSlotRepository.deleteById(id);
    }

    // ── Filter Slots ───────────────────────────────────────────────────────
    public List<BusSlotResponse> filterSlots(BusSlotFilterRequest filter) {
        List<BusSlot> slots = busSlotRepository.findAll();

        if (filter.getRouteId() != null) {
            slots = slots.stream()
                    .filter(s -> s.getRoute().getId().equals(filter.getRouteId()))
                    .collect(Collectors.toList());
        }

        if (filter.getBusId() != null) {
            slots = slots.stream()
                    .filter(s -> s.getBus() != null && s.getBus().getId().equals(filter.getBusId()))
                    .collect(Collectors.toList());
        }

        if (filter.getStatus() != null) {
            slots = slots.stream()
                    .filter(s -> s.getStatus() == filter.getStatus())
                    .collect(Collectors.toList());
        }

        if (filter.getFromTime() != null && filter.getToTime() != null) {
            slots = slots.stream()
                    .filter(s -> !s.getPickupTime().isBefore(filter.getFromTime()) &&
                                 !s.getPickupTime().isAfter(filter.getToTime()))
                    .collect(Collectors.toList());
        }

        if (filter.getIsRegular() != null) {
            slots = slots.stream()
                    .filter(s -> s.isRegular() == filter.getIsRegular())
                    .collect(Collectors.toList());
        }

        return slots.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Get Statistics ─────────────────────────────────────────────────────
    public BusSlotStatistics getStatistics() {
        return BusSlotStatistics.builder()
                .totalSlots(busSlotRepository.count())
                .activeSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.ACTIVE))
                .inactiveSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.INACTIVE))
                .fullSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.FULL))
                .regularSlots(busSlotRepository.findByIsRegularTrue().size())
                .build();
    }

    // ── Get Statistics by Route ────────────────────────────────────────────
    public BusSlotStatistics getStatisticsByRoute(Long routeId) {
        long slotsCount = busSlotRepository.countByRouteId(routeId);
        long activeCount = busSlotRepository.countByRouteIdAndStatus(routeId, BUS_SLOT_STATUS.ACTIVE);

        return BusSlotStatistics.builder()
                .slotsByRoute(slotsCount)
                .activeSlots(activeCount)
                .build();
    }

    // ── Get Statistics by Bus ──────────────────────────────────────────────
    public BusSlotStatistics getStatisticsByBus(Long busId) {
        long slotsCount = busSlotRepository.countByBusId(busId);
        long activeCount = busSlotRepository.countByBusIdAndStatus(busId, BUS_SLOT_STATUS.ACTIVE);

        return BusSlotStatistics.builder()
                .slotsByRoute(slotsCount)
                .activeSlots(activeCount)
                .build();
    }

    // ── Helper Methods ─────────────────────────────────────────────────────
    private BusSlot getOrThrow(Long id) {
        return busSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus slot not found with id: " + id));
    }

    private BusSlotResponse toResponse(BusSlot slot) {
        BusSlotResponse.BusSlotResponseBuilder builder = BusSlotResponse.builder()
                .id(slot.getId())
                .route(mapToRouteBasicResponse(slot.getRoute()))  // ✅ was mapToRouteResponse
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

        if (slot.getBus() != null) {
            builder.bus(mapToBusBasicResponse(slot.getBus()));    // ✅ was mapToBusResponse
        }

        return builder.build();
    }

    // ✅ was mapToRouteResponse returning full RouteResponse
    private RouteBasicResponse mapToRouteBasicResponse(Route route) {
        return RouteBasicResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .build();
    }

    // ✅ was mapToBusResponse returning full BusResponse
    private BusBasicResponse mapToBusBasicResponse(Bus bus) {
        return BusBasicResponse.builder()
                .id(bus.getId())
                .busName(bus.getBusName())
                .busNumber(bus.getBusNumber())
                .build();
    }
}