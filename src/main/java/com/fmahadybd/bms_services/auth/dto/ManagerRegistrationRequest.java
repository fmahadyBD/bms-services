package com.fmahadybd.bms_services.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRegistrationRequest {
    
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    
    @Email(message = "Email is not well formatted")
    @NotBlank(message = "Email is mandatory")
    private String email;
    
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;
    
    // Manager specific fields
    @NotBlank(message = "Employee ID is mandatory")
    private String employeeId;
    
    @NotBlank(message = "Department is mandatory")
    private String department;
    
    @NotBlank(message = "Position is mandatory")
    private String position;
}