package com.fmahadybd.bms_services.manager.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerResponse {
    private Long id;
    private String managerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String department;
    private String designation;
    private boolean isBlocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}