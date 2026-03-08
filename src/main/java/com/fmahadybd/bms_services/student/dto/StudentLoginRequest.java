package com.fmahadybd.bms_services.student.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Password is required")
    private String password;
}