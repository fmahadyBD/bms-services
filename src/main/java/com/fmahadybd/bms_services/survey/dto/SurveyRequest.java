package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    private String academicYear;

    private String semester;

    @Min(value = 1, message = "Target responses must be at least 1")
    private Integer targetResponses;

    private SurveyStatus status;

    private List<SurveyQuestionRequest> questions;
}