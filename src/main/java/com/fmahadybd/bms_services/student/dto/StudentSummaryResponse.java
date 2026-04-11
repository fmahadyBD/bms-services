package com.fmahadybd.bms_services.student.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryResponse {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private String batch;
    private String shift;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private Long routeId;
    private String routeName;
    private String busNo;
}