package com.fmahadybd.bms_services.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySchedule {
    private String day;
    private String pickupTime;
    private String dropTime;
    private String busNumber;
    private String driverContact;
}