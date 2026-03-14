package com.fmahadybd.bms_services.bus.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusStatistics {
    private long totalBuses;
    private long activeBuses;
    private long inactiveBuses;
    private long maintenanceBuses;
    private long onTripBuses;
    private long outOfServiceBuses;
    private long busesByRoute;
    private long availableBuses;  
}