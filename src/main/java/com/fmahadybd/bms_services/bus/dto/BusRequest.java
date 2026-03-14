package com.fmahadybd.bms_services.bus.dto;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRequest {

    @NotBlank(message = "Bus name is required")
    @Size(max = 100)
    private String busName;

    @NotBlank(message = "Bus number is required")
    @Size(max = 20)
    private String busNumber;

    @NotNull(message = "Status is required")
    private BUS_STATUS status;

    @NotBlank(message = "Driver name is required")
    @Size(max = 100)
    private String driverName;

    @NotBlank(message = "Helper name is required")
    @Size(max = 100)
    private String helperName;

    @NotBlank(message = "Driver phone is required")
    @Size(max = 15)
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String driverPhone;

    @NotBlank(message = "Helper phone is required")
    @Size(max = 15)
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String helperPhone;

    private Long routeId; // Optional: assign to a route
}