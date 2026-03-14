package com.fmahadybd.bms_services.slot.dto;

import com.fmahadybd.bms_services.route.dto.RouteResponse;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
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
    private String slotName;
    private LocalTime pickupTime;
    private LocalTime dropTime;
    private String fromLocation;
    private String toLocation;
    private BUS_SLOT_STATUS status;
    private String description;
    private boolean isRegular;
    private String regularDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}