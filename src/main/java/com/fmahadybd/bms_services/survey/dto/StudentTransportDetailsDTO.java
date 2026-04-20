// com/fmahadybd/bms_services/survey/dto/StudentTransportDetailsDTO.java
package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.survey.model.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTransportDetailsDTO {
    private Long assignmentId;
    private Long surveyId;
    private String surveyTitle;
    
    // Transport details
    private RouteBasicResponse route;
    private BusSlotResponse slot;
    private BusBasicResponse bus;
    private String assignedDay;
    private String boardingPoint;
    private String dropPoint;
    private String pickupTime;
    
    // Status
    private AssignmentStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    
    // Schedule
    private List<DailySchedule> weeklySchedule;
}
