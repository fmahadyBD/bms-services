package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusUtilizationView {
    private BusBasicResponse bus;
    private RouteBasicResponse route;
    private BusSlotResponse slot;
    private long assignedStudents;
    private long capacity;
    private double utilizationPercentage;
    private String status;
}