package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalResponses;
    private long approvedAssignments;
    private long pendingAssignments;
    private long waitlistedAssignments;
    private long totalBusesUsed;
    private double overallCapacityUtilization;
}