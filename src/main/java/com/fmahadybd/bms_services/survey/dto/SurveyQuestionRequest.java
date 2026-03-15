package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.QuestionType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private String options; // JSON string for multiple choice

    private Integer displayOrder;

    private boolean required;
}