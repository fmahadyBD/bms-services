package com.fmahadybd.bms_services.slot.dto;


import com.fmahadybd.bms_services.route.dto.RouteResponse;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.route.dto.PickupPointResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotResponse {
    private Long id;
    private RouteResponse route;
    private PickupPointResponse pickupPoint;
    private String slotName;
    private LocalTime pickupTime;
    private LocalTime dropTime;
    private Integer maxCapacity;
    private Integer currentBookings;
    private Integer availableSeats;
    private BUS_SLOT_STATUS status;
    private String description;
    private boolean isRecurring;
    private String recurringDays;
    private LocalTime cutoffTime;
    private Integer bufferMinutes;
    private Integer durationMinutes;
    private Double fareAmount;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private boolean isAvailable;
}