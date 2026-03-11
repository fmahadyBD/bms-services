
package com.fmahadybd.bms_services.route.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PickupPointRequest {

    @NotBlank(message = "Place name is required")
    @Size(max = 100)
    private String placeName;

    @Size(max = 255)
    private String placeDetails;

    @NotNull(message = "Pickup time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$",
             message = "Time format must be HH:mm (e.g. 07:30)")
    private String pickupTime;      // String → parsed to LocalTime in service

    @NotNull(message = "Stop order is required")
    @Min(value = 1, message = "Stop order must be at least 1")
    private Integer stopOrder;
}