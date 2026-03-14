package com.fmahadybd.bms_services.new_bus_request.dto;

import com.fmahadybd.bms_services.student.dto.StudentResponse;
import com.fmahadybd.bms_services.route.dto.RouteResponse;
import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import com.fmahadybd.bms_services.route.dto.PickupPointResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRequestResponse {
    private Long id;
    private StudentResponse student;
    private RouteResponse route;
    private PickupPointResponse pickupPoint;
    private LocalDate requestedFrom;
    private LocalDate requestedTo;
    private String reason;
    private BUS_REQUEST_STATUS status;
    private String adminRemarks;
    private LocalDate approvalDate;
    private Long approvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}