package com.fmahadybd.bms_services.survey.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SurveyStatistics {
    private Long surveyId;
    private String surveyTitle;
    private int totalResponses;
    private int targetResponses;
    private double completionRate;
    private Map<String, Long> responsesByStatus;
    private Map<String, Long> responsesByRoute;
    private Map<String, Long> responsesBySlot;
    private List<DropPointStat> topDropPoints;
    private List<BoardingPointStat> topBoardingPoints;
    private Map<String, Long> responsesByDepartment;
    private Map<String, Long> responsesBySemester;

    @Data
    @Builder
    public static class DropPointStat {
        private String dropPoint;
        private long count;
    }

    @Data
    @Builder
    public static class BoardingPointStat {
        private String boardingPoint;
        private long count;
    }
}