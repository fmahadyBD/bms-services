package com.fmahadybd.bms_services.routine.dto;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTINE_TYPE;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoutineRequest {

    @NotBlank(message = "Course name is required")
    @Size(max = 100)
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Pattern(regexp = "^[A-Z]{2,4}-\\d{4}$", message = "Course code format: CSE-1101")
    private String courseCode;

    @NotBlank(message = "Teacher name is required")
    @Size(max = 50)
    private String teacherName;

    @NotNull(message = "Day is required")
    private DAY day;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Room number is required")
    @Size(max = 100)
    private String roomNumber;

    @NotBlank(message = "Department is required")
    @Size(max = 50)
    private String department;

    @NotBlank(message = "Batch is required")
    @Pattern(regexp = "^\\d{4}$|^(Spring|Summer|Fall)-\\d{4}$")
    private String batch;

    private ROUTINE_TYPE routineType = ROUTINE_TYPE.CLASS;

    private Long routeId; // Optional
}