package com.fmahadybd.bms_services.bus.dto;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private BUS_STATUS status;

    private String reason;
}