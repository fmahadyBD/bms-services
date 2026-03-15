package com.fmahadybd.bms_services.survey.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveySubmissionRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @Email(message = "Invalid email format")
    private String studentEmail;

    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be 11 digits")
    private String studentPhone;

    private String studentDepartment;

    private String studentSemester;

    private Long selectedRouteId;

    private Long selectedSlotId;

    private Long preferredBusId;

    private String boardingPoint;

    private String dropPoint;

    private String pickupTime;

    private String responseData; // JSON string for additional responses

    private String additionalNotes;
}