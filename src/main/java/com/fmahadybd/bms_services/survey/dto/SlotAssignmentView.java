package com.fmahadybd.bms_services.survey.dto;

import java.util.List;

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
public class SlotAssignmentView {
    private BusSlotResponse slot;
    private RouteBasicResponse route;
    private long totalAssigned;
    private long capacityLimit;
    private long availableSeats;
    private double utilizationPercentage;
    private List<BusAssignmentSummary> assignedBuses;
}


