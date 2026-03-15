package com.fmahadybd.bms_services.survey.model;

import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_responses", indexes = {
    @Index(name = "idx_response_student", columnList = "student_id"),
    @Index(name = "idx_response_survey", columnList = "survey_id")
})
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
    private String studentId; // Student ID from university

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "student_email")
    private String studentEmail;

    @Column(name = "student_phone")
    private String studentPhone;

    @Column(name = "student_department")
    private String studentDepartment;

    @Column(name = "student_semester")
    private String studentSemester;

    // References to existing Route and Slot (without modifying them)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_route_id")
    private Route selectedRoute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_slot_id")
    private BusSlot selectedSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_bus_id")
    private Bus preferredBus;

    @Column(name = "boarding_point")
    private String boardingPoint; // Where student will board

    @Column(name = "drop_point")
    private String dropPoint; // Where student will get down

    @Column(name = "pickup_time")
    private String pickupTime; // Preferred pickup time

    @Column(name = "response_data", length = 5000)
    private String responseData; // JSON string for additional responses

    @Enumerated(EnumType.STRING)
    private ResponseStatus status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;
}