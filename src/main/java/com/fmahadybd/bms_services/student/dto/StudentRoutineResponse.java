package com.fmahadybd.bms_services.student.dto;

import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRoutineResponse {
    private Long routineId;
    private String courseName;
    private String courseCode;
    private String teacherName;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomNumber;
    private String routineType;
}