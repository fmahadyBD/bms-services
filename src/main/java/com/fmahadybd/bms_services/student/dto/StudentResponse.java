// ─── StudentResponse.java  (safe — never expose password) ──────────────────
package com.fmahadybd.bms_services.student.dto;

import com.fmahadybd.bms_services.enums.GENDER;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String department;
    private String batch;
    private GENDER gender;
    private boolean isBlocked;
    private String shift;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}