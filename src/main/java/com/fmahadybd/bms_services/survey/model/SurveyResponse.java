// com/fmahadybd/bms_services/survey/model/SurveyResponse.java
package com.fmahadybd.bms_services.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonIgnore
    private Survey survey;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "student_email")
    private String studentEmail;

    @Column(name = "student_phone")
    private String studentPhone;

    @Column(name = "selected_route_id")
    private Long selectedRouteId;

    @Column(name = "selected_slot_id")
    private Long selectedSlotId;

    @Column(name = "response_data", length = 5000)
    private String responseData; // JSON string of answers

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
}