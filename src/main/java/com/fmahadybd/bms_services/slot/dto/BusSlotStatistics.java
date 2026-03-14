package com.fmahadybd.bms_services.slot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusSlotStatistics {
    private long totalSlots;
    private long activeSlots;
    private long inactiveSlots;
    private long fullSlots;
    private long regularSlots;
    private long slotsByRoute;
}