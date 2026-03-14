package com.fmahadybd.bms_services.slot.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotBookingUpdateRequest {

    @NotNull(message = "Action is required")
    private String action;  // "INCREMENT" or "DECREMENT"

    private Integer count = 1;
}