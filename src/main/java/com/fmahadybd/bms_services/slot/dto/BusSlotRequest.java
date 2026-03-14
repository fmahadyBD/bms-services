package com.fmahadybd.bms_services.slot.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotRequest {

    @NotNull(message = "Route ID is required")
    private Long routeId;

    @NotNull(message = "Pickup point ID is required")
    private Long pickupPointId;

    @NotBlank(message = "Slot name is required")
    @Size(max = 100)
    private String slotName;

    @NotNull(message = "Pickup time is required")
    private LocalTime pickupTime;

    private LocalTime dropTime;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1)
    @Max(value = 100)
    private Integer maxCapacity;

    private BUS_SLOT_STATUS status;

    @Size(max = 500)
    private String description;

    private boolean isRecurring;

    private String recurringDays;

    private LocalTime cutoffTime;

    private Integer bufferMinutes = 15;

    @NotNull(message = "Duration minutes is required")
    @Min(value = 5)
    private Integer durationMinutes;

    @NotNull(message = "Fare amount is required")
    @Min(value = 0)
    private Double fareAmount;

    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
}