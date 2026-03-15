package com.fmahadybd.bms_services.route.dto;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RouteStatistics {
    private Long routeId;
    private String routeName;
    private String busNo;
    private ROUTE_STATUS status;
    private long totalBuses;
    private long activeBuses;
    private long totalSlots;
    private long activeSlots;
    private long totalPickupPoints;
    private List<DAY> operatingDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}