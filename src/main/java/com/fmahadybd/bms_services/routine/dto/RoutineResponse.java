package com.fmahadybd.bms_services.routine.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineResponse {
    private Long id;
    private String courseName;
    private String courseCode;
    private String teacherName;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String department;
    private String batch;
    private String routineType;
    private boolean isActive;
    private Long routeId;
    private String busNo; // If route assigned
    private int studentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}