package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.survey.model.ResponseStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveySubmissionResponse {

    private Long id;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String studentDepartment;
    private String studentSemester;

    // References to existing entities
    private RouteBasicResponse selectedRoute;
    private BusSlotResponse selectedSlot;
    private BusBasicResponse preferredBus;

    private String boardingPoint;
    private String dropPoint;
    private String pickupTime;
    private ResponseStatus status;
    private String responseData;
    private LocalDateTime submittedAt;
    private String additionalNotes;
}