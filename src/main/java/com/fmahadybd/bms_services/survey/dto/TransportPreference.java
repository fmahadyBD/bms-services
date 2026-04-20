// com/fmahadybd/bms_services/survey/dto/TransportPreference.java
package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportPreference {
    private Long routeId;
    private Long slotId;
    private String preferredDay;
    private String boardingPoint;
    private String dropPoint;
    private String pickupTime;
}