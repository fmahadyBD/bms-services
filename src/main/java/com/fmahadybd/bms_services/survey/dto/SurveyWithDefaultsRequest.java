// com/fmahadybd/bms_services/survey/dto/SurveyWithDefaultsRequest.java
package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyWithDefaultsRequest {
    private String title;
    private String description;
    private SurveyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String academicYear;
    private String semester;
    private Integer targetResponses;
    
    // Custom questions (optional, beyond defaults)
    private List<SurveyQuestionRequest> customQuestions;
    
    // Transport defaults (always included)
    private SurveyTransportDefaults transportDefaults;
}