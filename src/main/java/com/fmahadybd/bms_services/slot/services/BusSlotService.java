package com.fmahadybd.bms_services.slot.services;



import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.PickupPointResponse;
import com.fmahadybd.bms_services.route.dto.RouteResponse;
import com.fmahadybd.bms_services.route.model.PickupPoint;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.dto.BusSlotBookingUpdateRequest;
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
        PickupPoint pickupPoint = PickupPoint.builder().id(request.getPickupPointId()).build();

        BusSlot slot = BusSlot.builder()
                .route(route)
                .pickupPoint(pickupPoint)
                .slotName(request.getSlotName())
                .pickupTime(request.getPickupTime())
                .dropTime(request.getDropTime())
                .maxCapacity(request.getMaxCapacity())
                .currentBookings(0)
                .status(request.getStatus() != null ? request.getStatus() : BUS_SLOT_STATUS.ACTIVE)
                .description(request.getDescription())
                .isRecurring(request.isRecurring())
                .recurringDays(request.getRecurringDays())
                .cutoffTime(request.getCutoffTime())
                .bufferMinutes(request.getBufferMinutes())
                .durationMinutes(request.getDurationMinutes())
                .fareAmount(request.getFareAmount())
                .vehicleNumber(request.getVehicleNumber())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .createdBy(managerId)
                .build();

        BusSlot savedSlot = busSlotRepository.save(slot);
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
        slot.setMaxCapacity(request.getMaxCapacity());
        slot.setDescription(request.getDescription());
        slot.setRecurring(request.isRecurring());
        slot.setRecurringDays(request.getRecurringDays());
        slot.setCutoffTime(request.getCutoffTime());
        slot.setBufferMinutes(request.getBufferMinutes());
        slot.setDurationMinutes(request.getDurationMinutes());
        slot.setFareAmount(request.getFareAmount());
        slot.setVehicleNumber(request.getVehicleNumber());
        slot.setDriverName(request.getDriverName());
        slot.setDriverPhone(request.getDriverPhone());
        slot.setUpdatedBy(managerId);

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

    // ── Get Slots by Status ────────────────────────────────────────────────
    public List<BusSlotResponse> getSlotsByStatus(BUS_SLOT_STATUS status) {
        return busSlotRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Available Slots ────────────────────────────────────────────────
    public List<BusSlotResponse> getAvailableSlots() {
        return busSlotRepository.findAvailableSlots().stream()
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

    // ── Update Slot Status ─────────────────────────────────────────────────
    @Transactional
    public BusSlotResponse updateSlotStatus(Long id, BusSlotStatusUpdateRequest statusUpdate, Long managerId) {
        BusSlot slot = getOrThrow(id);
        slot.setStatus(statusUpdate.getStatus());
        slot.setUpdatedBy(managerId);
        return toResponse(busSlotRepository.save(slot));
    }

    // ── Update Booking Count ───────────────────────────────────────────────
    @Transactional
    public BusSlotResponse updateBookingCount(Long id, BusSlotBookingUpdateRequest bookingUpdate) {
        BusSlot slot = getOrThrow(id);

        if ("INCREMENT".equalsIgnoreCase(bookingUpdate.getAction())) {
            for (int i = 0; i < bookingUpdate.getCount(); i++) {
                slot.incrementBooking();
            }
        } else if ("DECREMENT".equalsIgnoreCase(bookingUpdate.getAction())) {
            for (int i = 0; i < bookingUpdate.getCount(); i++) {
                slot.decrementBooking();
            }
        } else {
            throw new IllegalArgumentException("Use INCREMENT or DECREMENT");
        }

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

        if (filter.getPickupPointId() != null) {
            slots = slots.stream()
                    .filter(s -> s.getPickupPoint().getId().equals(filter.getPickupPointId()))
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

        if (filter.getIsAvailable() != null && filter.getIsAvailable()) {
            slots = slots.stream()
                    .filter(BusSlot::isAvailable)
                    .collect(Collectors.toList());
        }

        if (filter.getIsRecurring() != null) {
            slots = slots.stream()
                    .filter(s -> s.isRecurring() == filter.getIsRecurring())
                    .collect(Collectors.toList());
        }

        return slots.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Get Statistics ─────────────────────────────────────────────────────
    public BusSlotStatistics getStatistics() {
        Long totalCapacity = busSlotRepository.getTotalCapacity();
        Long totalBookings = busSlotRepository.getTotalBookings();
        Double averageFare = busSlotRepository.getAverageFare();

        return BusSlotStatistics.builder()
                .totalSlots(busSlotRepository.count())
                .activeSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.ACTIVE))
                .inactiveSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.INACTIVE))
                .fullSlots(busSlotRepository.countByStatus(BUS_SLOT_STATUS.FULL))
                .recurringSlots(busSlotRepository.findByIsRecurringTrue().size())
                .totalCapacity(totalCapacity != null ? totalCapacity : 0)
                .totalBookings(totalBookings != null ? totalBookings : 0)
                .availableSeats((totalCapacity != null ? totalCapacity : 0) - 
                                (totalBookings != null ? totalBookings : 0))
                .averageFare(averageFare != null ? averageFare : 0.0)
                .build();
    }

    // ── Get Statistics by Route ────────────────────────────────────────────
    public BusSlotStatistics getStatisticsByRoute(Long routeId) {
        Object[] stats = busSlotRepository.getStatisticsByRoute(routeId);
        
        return BusSlotStatistics.builder()
                .slotsByRoute((Long) stats[0])
                .totalCapacity((Long) stats[1])
                .totalBookings((Long) stats[2])
                .availableSeats((Long) stats[1] - (Long) stats[2])
                .build();
    }

    // ── Helper Methods ─────────────────────────────────────────────────────
    private BusSlot getOrThrow(Long id) {
        return busSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus slot not found with id: " + id));
    }

    private BusSlotResponse toResponse(BusSlot slot) {
        return BusSlotResponse.builder()
                .id(slot.getId())
                .route(mapToRouteResponse(slot.getRoute()))
                .pickupPoint(mapToPickupPointResponse(slot.getPickupPoint()))
                .slotName(slot.getSlotName())
                .pickupTime(slot.getPickupTime())
                .dropTime(slot.getDropTime())
                .maxCapacity(slot.getMaxCapacity())
                .currentBookings(slot.getCurrentBookings())
                .availableSeats(slot.getAvailableSeats())
                .status(slot.getStatus())
                .description(slot.getDescription())
                .isRecurring(slot.isRecurring())
                .recurringDays(slot.getRecurringDays())
                .cutoffTime(slot.getCutoffTime())
                .bufferMinutes(slot.getBufferMinutes())
                .durationMinutes(slot.getDurationMinutes())
                .fareAmount(slot.getFareAmount())
                .vehicleNumber(slot.getVehicleNumber())
                .driverName(slot.getDriverName())
                .driverPhone(slot.getDriverPhone())
                .createdAt(slot.getCreatedAt())
                .updatedAt(slot.getUpdatedAt())
                .createdBy(slot.getCreatedBy())
                .updatedBy(slot.getUpdatedBy())
                .isAvailable(slot.isAvailable())
                .build();
    }

    private RouteResponse mapToRouteResponse(Route route) {
        return RouteResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .routeLine(route.getRouteLine())
                .status(route.getStatus())
                .build();
    }

    private PickupPointResponse mapToPickupPointResponse(PickupPoint pickupPoint) {
        return PickupPointResponse.builder()
                .id(pickupPoint.getId())
                .placeName(pickupPoint.getPlaceName())
                .placeDetails(pickupPoint.getPlaceDetails())
                .pickupTime(pickupPoint.getPickupTime())
                .stopOrder(pickupPoint.getStopOrder())
                .build();
    }
}