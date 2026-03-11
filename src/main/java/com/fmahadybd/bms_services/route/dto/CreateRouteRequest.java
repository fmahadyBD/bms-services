// ─── CreateRouteRequest.java ──────────────────────────────────────────────────
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
    @Pattern(regexp = "^BUS-\\d{2,3}$", message = "Bus No format: BUS-01 or BUS-001")
    private String busNo;

    @NotBlank(message = "Route name is required")
    @Size(max = 100)
    private String routeName;

    @NotBlank(message = "Route line is required")
    @Size(max = 255)
    private String routeLine;

    @NotEmpty(message = "At least one pickup point is required")
    @Valid                          // ← triggers validation inside each PickupPointRequest
    private List<PickupPointRequest> pickupPoints;

    @NotEmpty(message = "At least one operating day is required")
    private List<DAY> operatingDays;
}