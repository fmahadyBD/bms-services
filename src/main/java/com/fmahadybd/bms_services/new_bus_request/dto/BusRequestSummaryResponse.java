package com.fmahadybd.bms_services.new_bus_request.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRequestSummaryResponse {
    private Long id;
    private String studentName;
    private String studentId;
    private String studentEmail;
    private String routeName;
    private String busNo;
    private String pickupPointName;
    private LocalDate requestedFrom;
    private LocalDate requestedTo;
    private BUS_REQUEST_STATUS status;
    private LocalDateTime createdAt;
}