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
    @FutureOrPresent(message = "Request from date must be present or future")
    private LocalDate requestedFrom;

    @NotNull(message = "Request to date is required")
    @Future(message = "Request to date must be future")
    private LocalDate requestedTo;

    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
}