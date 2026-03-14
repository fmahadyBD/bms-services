package com.fmahadybd.bms_services.route.dto;

import com.fmahadybd.bms_services.enums.DAY;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDayResponse {
    private Long id;
    private DAY day;
    private boolean isActive;
}