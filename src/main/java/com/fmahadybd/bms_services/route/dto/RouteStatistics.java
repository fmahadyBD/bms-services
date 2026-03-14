package com.fmahadybd.bms_services.route.dto;

import com.fmahadybd.bms_services.enums.DAY;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RouteStatistics {
    private Long routeId;
    private String routeName;
    private long totalBuses;
    private long activeBuses;
    private long totalSlots;
    private long activeSlots;
    private long totalPickupPoints;
    private List<DAY> operatingDays;
}