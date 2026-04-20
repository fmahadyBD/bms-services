package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String questionText;
    private String questionType;
    private String options;
    private Integer displayOrder;
    private boolean required;
}