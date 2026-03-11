
package com.fmahadybd.bms_services.route.dto;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RouteResponse {
    private Long id;
    private String busNo;
    private String routeName;
    private String routeLine;
    private ROUTE_STATUS status;
    private List<PickupPointResponse> pickupPoints;
    private List<DAY> operatingDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}