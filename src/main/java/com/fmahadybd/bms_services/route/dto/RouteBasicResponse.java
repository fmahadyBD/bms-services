package com.fmahadybd.bms_services.route.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteBasicResponse {
    private Long id;
    private String busNo;
    private String routeName;
}