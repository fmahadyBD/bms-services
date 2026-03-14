package com.fmahadybd.bms_services.slot.dto;


import lombok.*;

import java.time.LocalTime;

import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotSummaryResponse {
    private Long id;
    private String routeName;
    private String busNo;
    private String pickupPointName;
    private String slotName;
    private LocalTime pickupTime;
    private Integer maxCapacity;
    private Integer currentBookings;
    private Integer availableSeats;
    private BUS_SLOT_STATUS status;
    private Double fareAmount;
    private boolean isAvailable;
}