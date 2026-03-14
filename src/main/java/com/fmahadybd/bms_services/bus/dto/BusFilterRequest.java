package com.fmahadybd.bms_services.bus.dto;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusFilterRequest {
    private String busName;
    private String busNumber;
    private BUS_STATUS status;
    private Long routeId;
    private String driverName;
}