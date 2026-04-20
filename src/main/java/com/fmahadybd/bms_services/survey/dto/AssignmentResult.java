// com/fmahadybd/bms_services/survey/dto/AssignmentResult.java
package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.survey.model.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResult {
    private Long assignmentId;
    private Bus assignedBus;
    private AssignmentStatus status;
    private String message;
}