
package com.fmahadybd.bms_services.route.dto;

import lombok.*;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PickupPointResponse {
    private Long id;
    private String placeName;
    private String placeDetails;
    private LocalTime pickupTime;
    private Integer stopOrder;
}