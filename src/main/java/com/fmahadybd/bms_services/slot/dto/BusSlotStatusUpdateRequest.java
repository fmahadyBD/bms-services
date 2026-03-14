package com.fmahadybd.bms_services.slot.dto;


import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private BUS_SLOT_STATUS status;

    private String reason;
}