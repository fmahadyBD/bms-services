// com/fmahadybd/bms_services/survey/dto/SubmissionRequest.java
package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequest {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private Long selectedRouteId;
    private Long selectedSlotId;
    private Map<String, Object> answers; // questionId -> answer
}