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

    private Long busId;  // New field - optional

    @NotBlank(message = "Slot name is required")
    @Size(max = 100)
    private String slotName;

    @NotNull(message = "Pickup time is required")
    private LocalTime pickupTime;

    private LocalTime dropTime;

    @NotBlank(message = "From location is required")
    private String fromLocation;

    @NotBlank(message = "To location is required")
    private String toLocation;

    private BUS_SLOT_STATUS status;

    @Size(max = 500)
    private String description;

    private boolean isRegular = true;

    private String regularDays;
}