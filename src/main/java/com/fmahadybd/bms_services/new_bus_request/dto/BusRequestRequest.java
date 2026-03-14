package com.fmahadybd.bms_services.new_bus_request.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRequestRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Route ID is required")
    private Long routeId;

    @NotNull(message = "Pickup point ID is required")
    private Long pickupPointId;

    @NotNull(message = "Request from date is required")
    // Remove @FutureOrPresent if you want to allow past dates
    // Or keep it and use future dates
    private LocalDate requestedFrom;

    @NotNull(message = "Request to date is required")
    // Remove @Future if you want to allow past dates
    // Or keep it and use future dates
    private LocalDate requestedTo;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
}