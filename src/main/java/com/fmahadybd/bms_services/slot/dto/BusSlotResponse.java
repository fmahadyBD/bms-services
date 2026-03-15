package com.fmahadybd.bms_services.slot.dto;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
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
    private RouteBasicResponse route;   // ✅ was RouteResponse
    private BusBasicResponse bus;      
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