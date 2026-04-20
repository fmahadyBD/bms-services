package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusAssignmentSummary {
    private Long busId;
    private String busNumber;
    private long assignedCount;
    private long capacity;
}