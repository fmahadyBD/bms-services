// com/fmahadybd/bms_services/survey/dto/ManagerDashboardDTO.java - Fixed
package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDashboardDTO {
    private Long surveyId;
    private String surveyTitle;
    private DashboardStats stats;
    private List<SlotAssignmentView> slotAssignments;
    private List<BusUtilizationView> busUtilizations;
    private List<PendingAssignmentView> pendingAssignments;
}



