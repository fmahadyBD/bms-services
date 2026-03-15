package com.fmahadybd.bms_services.bus.dto;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusResponse {
    private Long id;
    private String busName;
    private String busNumber;
    private BUS_STATUS status;
    private String driverName;
    private String helperName;
    private String driverPhone;
    private String helperPhone;
    private Long routeId;          
    private List<BusSlotSummary> busSlots;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BusSlotSummary {
        private Long id;
        private String slotName;
        private String fromLocation;
        private String toLocation;
        private String pickupTime;
        private String status;
    }
}