package com.fmahadybd.bms_services.student.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {

    @Size(min = 2, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Name contains invalid characters")
    private String name;

    @Email
    @Size(max = 150)
    private String email;

    @Pattern(regexp = "^(?:\\+880|0)1[3-9]\\d{8}$",
             message = "Invalid Bangladeshi phone number")
    private String phoneNumber;

    @Size(max = 255)
    private String address;

    @Pattern(regexp = "^(Morning|Day|Evening)$")
    private String shift;
}