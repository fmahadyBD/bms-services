package com.fmahadybd.bms_services.manager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerStatusUpdateRequest {

    @NotNull(message = "Blocked status is required")
    private Boolean blocked;

    private String reason;
}