package com.fmahadybd.bms_services.survey.dto;

import com.fmahadybd.bms_services.survey.model.QuestionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestionResponse {
    private Long id;
    private String questionText;
    private QuestionType questionType;
    private String options;
    private Integer displayOrder;
    private boolean required;
    private boolean isActive;
}