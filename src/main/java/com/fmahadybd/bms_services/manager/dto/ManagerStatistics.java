package com.fmahadybd.bms_services.manager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerStatistics {
    private long totalManagers;
    private long activeManagers;
    private long blockedManagers;
    private long managersByDepartment;
}