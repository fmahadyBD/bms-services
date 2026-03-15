package com.fmahadybd.bms_services.route.dto;

import com.fmahadybd.bms_services.enums.DAY;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRouteRequest {

    @NotBlank(message = "Bus number is required")
    @Pattern(regexp = "^BUS-\\d{3}$", message = "Bus No must follow format: BUS-001, BUS-002, etc.")
    private String busNo;

    @NotBlank(message = "Route name is required")
    @Size(min = 3, max = 100, message = "Route name must be between 3 and 100 characters")
    private String routeName;

    @NotBlank(message = "Route line is required")
    @Size(min = 5, max = 255, message = "Route line must be between 5 and 255 characters")
    private String routeLine;

    @NotEmpty(message = "At least one operating day is required")
    private List<DAY> operatingDays;

    @NotEmpty(message = "At least one pickup point is required")
    @Valid
    private List<PickupPointRequest> pickupPoints;
}