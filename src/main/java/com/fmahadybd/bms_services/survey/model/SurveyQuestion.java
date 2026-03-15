package com.fmahadybd.bms_services.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "survey_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnore
    private Survey survey;

    @Column(nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Column(length = 1000)
    private String options; // JSON string for multiple choice options

    private Integer displayOrder;

    private boolean required;

    @Column(name = "is_active")
    private boolean isActive;
}