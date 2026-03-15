package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SurveySummaryResponse {
    private Long id;
    private String title;
    private SurveyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private int totalResponses;
    private int targetResponses;
    private double completionRate;
    private boolean isActive;
}