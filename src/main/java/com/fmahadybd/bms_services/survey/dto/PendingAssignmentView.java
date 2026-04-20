package com.fmahadybd.bms_services.survey.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingAssignmentView {
    private Long assignmentId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String requestedRoute;
    private String requestedSlot;
    private String requestedDay;
    private LocalDateTime submittedAt;
}