// com/fmahadybd/bms_services/survey/dto/SurveyTransportDefaults.java
package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyTransportDefaults {
    private List<Long> availableRouteIds;
    private List<Long> availableSlotIds;
    private List<String> availableDays; // Sunday, Monday, etc.
    private Integer busCapacityLimit;
    private boolean autoAssignBuses;
}