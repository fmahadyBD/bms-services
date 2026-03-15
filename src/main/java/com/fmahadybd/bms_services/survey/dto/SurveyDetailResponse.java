package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDetailResponse {

    private Long id;
    private String title;
    private String description;
    private SurveyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String academicYear;
    private String semester;
    private Integer totalResponses;
    private Integer targetResponses;
    private boolean isActive;
    private List<SurveyQuestionResponse> questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}