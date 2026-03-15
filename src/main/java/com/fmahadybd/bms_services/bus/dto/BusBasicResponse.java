package com.fmahadybd.bms_services.bus.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusBasicResponse {
    private Long id;
    private String busName;
    private String busNumber;
}