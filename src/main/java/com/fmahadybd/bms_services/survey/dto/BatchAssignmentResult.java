// com/fmahadybd/bms_services/survey/dto/BatchAssignmentResult.java
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
public class BatchAssignmentResult {
    private int totalProcessed;
    private int successfullyAssigned;
    private int waitlisted;
    private List<AssignmentResult> results;
}