package com.fmahadybd.bms_services.new_bus_request.dto;

import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRequestStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private BUS_REQUEST_STATUS status;

    private String adminRemarks;
}