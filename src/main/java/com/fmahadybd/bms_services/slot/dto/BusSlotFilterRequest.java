package com.fmahadybd.bms_services.slot.dto;

import lombok.*;
import java.time.LocalTime;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusSlotFilterRequest {
    private Long routeId;
    private BUS_SLOT_STATUS status;
    private LocalTime fromTime;
    private LocalTime toTime;
    private Boolean isRegular;
}