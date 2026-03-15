package com.fmahadybd.bms_services.route.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupPointRequest {

    @NotBlank(message = "Place name is required")
    @Size(max = 100, message = "Place name cannot exceed 100 characters")
    private String placeName;

    @Size(max = 255, message = "Place details cannot exceed 255 characters")
    private String placeDetails;

    @NotBlank(message = "Pickup time is required")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Pickup time must be in format HH:MM")
    @JsonFormat(pattern = "HH:mm")
    private String pickupTime;

    @NotNull(message = "Stop order is required")
    @Min(value = 1, message = "Stop order must be at least 1")
    private Integer stopOrder;
}