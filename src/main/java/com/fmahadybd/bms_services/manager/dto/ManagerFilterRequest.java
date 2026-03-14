package com.fmahadybd.bms_services.manager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerFilterRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private String designation;
    private Boolean isBlocked;
}