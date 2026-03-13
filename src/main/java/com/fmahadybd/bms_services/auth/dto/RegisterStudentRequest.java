package com.fmahadybd.bms_services.auth.dto;

import com.fmahadybd.bms_services.enums.GENDER;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterStudentRequest {

    @NotBlank(message = "Student ID is required")
    @Pattern(regexp = "^[A-Z]{2,10}-\\d{4}-\\d{3}$",
             message = "Student ID format invalid. Expected: DEPT-YEAR-SEQ (e.g. CSE-2021-001)")
    private String studentId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Name contains invalid characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+880|0)1[3-9]\\d{8}$",
             message = "Invalid Bangladeshi phone number")
    private String phoneNumber;

    @Size(max = 255)
    private String address;

    @NotBlank(message = "Department is required")
    @Size(max = 50)
    private String department;

    @NotBlank(message = "Batch is required")
    @Pattern(regexp = "^\\d{4}$|^(Spring|Summer|Fall)-\\d{4}$",
             message = "Batch format invalid. Expected: '2021' or 'Spring-2022'")
    private String batch;

    @NotNull(message = "Gender is required")
    private GENDER gender;

    @NotBlank(message = "Shift is required")
    @Pattern(regexp = "^(Morning|Day|Evening)$",
             message = "Shift must be Morning, Day, or Evening")
    private String shift;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be 8–64 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "Password must have uppercase, lowercase, digit, and special character")
    private String password;
}