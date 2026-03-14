package com.fmahadybd.bms_services.new_bus_request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusRequestStatistics {
    private long totalRequests;
    private long pendingRequests;
    private long approvedRequests;
    private long rejectedRequests;
    private long activeRequests;
}