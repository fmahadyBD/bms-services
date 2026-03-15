package com.fmahadybd.bms_services.survey.service;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.bus.repository.BusRepository;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.slot.repository.BusSlotRepository;
import com.fmahadybd.bms_services.survey.dto.*;
import com.fmahadybd.bms_services.survey.model.*;
import com.fmahadybd.bms_services.survey.repository.SurveyRepository;
import com.fmahadybd.bms_services.survey.repository.SurveyResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository responseRepository;
    private final RouteRepository routeRepository;
    private final BusSlotRepository busSlotRepository;
    private final BusRepository busRepository;
    private final ObjectMapper objectMapper;

    // ─────────────────────────────────────────────────
    // SURVEY MANAGEMENT
    // ─────────────────────────────────────────────────

    @Transactional
    public SurveyDetailResponse createSurvey(SurveyRequest request, Long userId) {
        log.info("Creating new survey: {}", request.getTitle());

        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : SurveyStatus.DRAFT)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .targetResponses(request.getTargetResponses())
                .totalResponses(0)
                .isActive(true)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        // Add questions if provided
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<SurveyQuestion> questions = request.getQuestions().stream()
                    .map(q -> SurveyQuestion.builder()
                            .survey(survey)
                            .questionText(q.getQuestionText())
                            .questionType(q.getQuestionType())
                            .options(q.getOptions())
                            .displayOrder(q.getDisplayOrder())
                            .required(q.isRequired())
                            .isActive(true)
                            .build())
                    .collect(Collectors.toList());
            survey.setQuestions(questions);
        }

        Survey savedSurvey = surveyRepository.save(survey);
        return mapToSurveyDetailResponse(savedSurvey);
    }

    @Transactional
    public SurveyDetailResponse updateSurvey(Long id, SurveyRequest request, Long userId) {
        log.info("Updating survey with id: {}", id);

        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setStartDate(request.getStartDate());
        survey.setEndDate(request.getEndDate());
        survey.setAcademicYear(request.getAcademicYear());
        survey.setSemester(request.getSemester());
        survey.setTargetResponses(request.getTargetResponses());
        survey.setUpdatedBy(userId);

        Survey updatedSurvey = surveyRepository.save(survey);
        return mapToSurveyDetailResponse(updatedSurvey);
    }

    @Transactional
    public SurveyDetailResponse updateSurveyStatus(Long id, SurveyStatus status, Long userId) {
        log.info("Updating survey status: {} for survey id: {}", status, id);

        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        survey.setStatus(status);
        survey.setUpdatedBy(userId);

        Survey updatedSurvey = surveyRepository.save(survey);
        return mapToSurveyDetailResponse(updatedSurvey);
    }

    public SurveyDetailResponse getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));
        return mapToSurveyDetailResponse(survey);
    }

    public List<SurveySummaryResponse> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    public List<SurveySummaryResponse> getActiveSurveys() {
        return surveyRepository.findByIsActiveTrue().stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    public List<SurveySummaryResponse> getCurrentSurveys() {
        LocalDate today = LocalDate.now();
        return surveyRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSurvey(Long id) {
        log.info("Deleting survey with id: {}", id);

        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

        // Soft delete - just mark as inactive
        survey.setActive(false);
        surveyRepository.save(survey);
    }

    // ─────────────────────────────────────────────────
    // SURVEY RESPONSE MANAGEMENT
    // ─────────────────────────────────────────────────

    @Transactional
    public SurveySubmissionResponse submitResponse(Long surveyId, SurveySubmissionRequest request) {
        log.info("Submitting response for survey: {} by student: {}", surveyId, request.getStudentId());

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

        // Check if survey is active and within date range
        if (!survey.isActive() || survey.getStatus() != SurveyStatus.PUBLISHED) {
            throw new IllegalStateException("Survey is not active or published");
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(survey.getStartDate()) || today.isAfter(survey.getEndDate())) {
            throw new IllegalStateException("Survey is not within the valid date range");
        }

        // Check if student already responded
        if (responseRepository.existsBySurveyIdAndStudentId(surveyId, request.getStudentId())) {
            throw new IllegalStateException("Student has already responded to this survey");
        }

        SurveyResponse response = new SurveyResponse();
        response.setSurvey(survey);
        response.setStudentId(request.getStudentId());
        response.setStudentName(request.getStudentName());
        response.setStudentEmail(request.getStudentEmail());
        response.setStudentPhone(request.getStudentPhone());
        response.setStudentDepartment(request.getStudentDepartment());
        response.setStudentSemester(request.getStudentSemester());
        response.setBoardingPoint(request.getBoardingPoint());
        response.setDropPoint(request.getDropPoint());
        response.setPickupTime(request.getPickupTime());
        response.setAdditionalNotes(request.getAdditionalNotes());
        response.setStatus(ResponseStatus.PENDING);
        response.setSubmittedAt(LocalDateTime.now());

        // Set Route if provided
        if (request.getSelectedRouteId() != null) {
            Route route = routeRepository.findById(request.getSelectedRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + request.getSelectedRouteId()));
            response.setSelectedRoute(route);
        }

        // Set Slot if provided
        if (request.getSelectedSlotId() != null) {
            BusSlot slot = busSlotRepository.findById(request.getSelectedSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus slot not found with id: " + request.getSelectedSlotId()));
            response.setSelectedSlot(slot);
        }

        // Set Bus if provided
        if (request.getPreferredBusId() != null) {
            Bus bus = busRepository.findById(request.getPreferredBusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + request.getPreferredBusId()));
            response.setPreferredBus(bus);
        }

        // Store additional response data as JSON
        if (request.getResponseData() != null) {
            response.setResponseData(request.getResponseData());
        }

        SurveyResponse savedResponse = responseRepository.save(response);

        // Update total responses count
        survey.setTotalResponses(survey.getTotalResponses() + 1);
        surveyRepository.save(survey);

        return mapToSubmissionResponse(savedResponse);
    }

    @Transactional
    public SurveySubmissionResponse updateResponseStatus(Long responseId, ResponseStatus status, String reason) {
        log.info("Updating response status: {} for response id: {}", status, responseId);

        SurveyResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found with id: " + responseId));

        response.setStatus(status);
        if (reason != null) {
            response.setAdditionalNotes(reason);
        }

        SurveyResponse updatedResponse = responseRepository.save(response);
        return mapToSubmissionResponse(updatedResponse);
    }

    public List<SurveySubmissionResponse> getResponsesBySurvey(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId).stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    public List<SurveySubmissionResponse> getResponsesByStudent(String studentId) {
        return responseRepository.findByStudentId(studentId).stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    public SurveySubmissionResponse getStudentResponse(Long surveyId, String studentId) {
        return responseRepository.findBySurveyIdAndStudentId(surveyId, studentId)
                .map(this::mapToSubmissionResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found for student: " + studentId));
    }

    public List<SurveySubmissionResponse> getResponsesByRoute(Long surveyId, Long routeId) {
        return responseRepository.findBySurveyAndRoute(surveyId, routeId).stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    public List<SurveySubmissionResponse> getResponsesBySlot(Long surveyId, Long slotId) {
        return responseRepository.findBySurveyAndSlot(surveyId, slotId).stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────
    // STATISTICS
    // ─────────────────────────────────────────────────

    public SurveyStatistics getSurveyStatistics(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

        List<SurveyResponse> responses = responseRepository.findBySurveyId(surveyId);

        // Count by status
        Map<String, Long> statusCount = responses.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getStatus().toString(),
                        Collectors.counting()
                ));

        // Count by route
        Map<String, Long> routeCount = responses.stream()
                .filter(r -> r.getSelectedRoute() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSelectedRoute().getRouteName(),
                        Collectors.counting()
                ));

        // Count by slot
        Map<String, Long> slotCount = responses.stream()
                .filter(r -> r.getSelectedSlot() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSelectedSlot().getSlotName(),
                        Collectors.counting()
                ));

        // Top drop points
        List<SurveyStatistics.DropPointStat> topDropPoints = responses.stream()
                .filter(r -> r.getDropPoint() != null)
                .collect(Collectors.groupingBy(
                        SurveyResponse::getDropPoint,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> SurveyStatistics.DropPointStat.builder()
                        .dropPoint(e.getKey())
                        .count(e.getValue())
                        .build())
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .limit(5)
                .collect(Collectors.toList());

        // Top boarding points
        List<SurveyStatistics.BoardingPointStat> topBoardingPoints = responses.stream()
                .filter(r -> r.getBoardingPoint() != null)
                .collect(Collectors.groupingBy(
                        SurveyResponse::getBoardingPoint,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> SurveyStatistics.BoardingPointStat.builder()
                        .boardingPoint(e.getKey())
                        .count(e.getValue())
                        .build())
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .limit(5)
                .collect(Collectors.toList());

        // Count by department
        Map<String, Long> deptCount = responses.stream()
                .filter(r -> r.getStudentDepartment() != null)
                .collect(Collectors.groupingBy(
                        SurveyResponse::getStudentDepartment,
                        Collectors.counting()
                ));

        // Count by semester
        Map<String, Long> semesterCount = responses.stream()
                .filter(r -> r.getStudentSemester() != null)
                .collect(Collectors.groupingBy(
                        SurveyResponse::getStudentSemester,
                        Collectors.counting()
                ));

        double completionRate = survey.getTargetResponses() != null && survey.getTargetResponses() > 0
                ? (double) responses.size() / survey.getTargetResponses() * 100
                : 0;

        return SurveyStatistics.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .totalResponses(responses.size())
                .targetResponses(survey.getTargetResponses() != null ? survey.getTargetResponses() : 0)
                .completionRate(completionRate)
                .responsesByStatus(statusCount)
                .responsesByRoute(routeCount)
                .responsesBySlot(slotCount)
                .topDropPoints(topDropPoints)
                .topBoardingPoints(topBoardingPoints)
                .responsesByDepartment(deptCount)
                .responsesBySemester(semesterCount)
                .build();
    }

    // ─────────────────────────────────────────────────
    // EXPORT / REPORTING
    // ─────────────────────────────────────────────────

    public List<Map<String, Object>> exportSurveyResponses(Long surveyId) {
        List<SurveyResponse> responses = responseRepository.findBySurveyId(surveyId);

        return responses.stream().map(response -> {
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", response.getStudentId());
            map.put("studentName", response.getStudentName());
            map.put("studentEmail", response.getStudentEmail());
            map.put("studentPhone", response.getStudentPhone());
            map.put("studentDepartment", response.getStudentDepartment());
            map.put("studentSemester", response.getStudentSemester());
            map.put("route", response.getSelectedRoute() != null ? response.getSelectedRoute().getRouteName() : null);
            map.put("slot", response.getSelectedSlot() != null ? response.getSelectedSlot().getSlotName() : null);
            map.put("bus", response.getPreferredBus() != null ? response.getPreferredBus().getBusNumber() : null);
            map.put("boardingPoint", response.getBoardingPoint());
            map.put("dropPoint", response.getDropPoint());
            map.put("pickupTime", response.getPickupTime());
            map.put("status", response.getStatus());
            map.put("submittedAt", response.getSubmittedAt());
            return map;
        }).collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────
    // MAPPING METHODS
    // ─────────────────────────────────────────────────

    private SurveyDetailResponse mapToSurveyDetailResponse(Survey survey) {
        List<SurveyQuestionResponse> questionResponses = survey.getQuestions() != null 
                ? survey.getQuestions().stream()
                    .map(this::mapToQuestionResponse)
                    .collect(Collectors.toList())
                : new ArrayList<>();

        return SurveyDetailResponse.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .status(survey.getStatus())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .academicYear(survey.getAcademicYear())
                .semester(survey.getSemester())
                .totalResponses(survey.getTotalResponses())
                .targetResponses(survey.getTargetResponses())
                .isActive(survey.isActive())
                .questions(questionResponses)
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .createdBy(survey.getCreatedBy())
                .updatedBy(survey.getUpdatedBy())
                .build();
    }

    private SurveySummaryResponse mapToSummaryResponse(Survey survey) {
        long responseCount = responseRepository.countResponsesBySurveyId(survey.getId());
        double completionRate = survey.getTargetResponses() != null && survey.getTargetResponses() > 0
                ? (double) responseCount / survey.getTargetResponses() * 100
                : 0;

        return SurveySummaryResponse.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .status(survey.getStatus())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .totalResponses((int) responseCount)
                .targetResponses(survey.getTargetResponses() != null ? survey.getTargetResponses() : 0)
                .completionRate(completionRate)
                .isActive(survey.isActive())
                .build();
    }

    private SurveyQuestionResponse mapToQuestionResponse(SurveyQuestion question) {
        return SurveyQuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .options(question.getOptions())
                .displayOrder(question.getDisplayOrder())
                .required(question.isRequired())
                .isActive(question.isActive())
                .build();
    }

    private SurveySubmissionResponse mapToSubmissionResponse(SurveyResponse response) {
        return SurveySubmissionResponse.builder()
                .id(response.getId())
                .studentId(response.getStudentId())
                .studentName(response.getStudentName())
                .studentEmail(response.getStudentEmail())
                .studentPhone(response.getStudentPhone())
                .studentDepartment(response.getStudentDepartment())
                .studentSemester(response.getStudentSemester())
                .selectedRoute(response.getSelectedRoute() != null ? 
                        RouteBasicResponse.builder()
                                .id(response.getSelectedRoute().getId())
                                .busNo(response.getSelectedRoute().getBusNo())
                                .routeName(response.getSelectedRoute().getRouteName())
                                .build() : null)
                .selectedSlot(response.getSelectedSlot() != null ? 
                        mapToSlotResponse(response.getSelectedSlot()) : null)
                .preferredBus(response.getPreferredBus() != null ? 
                        BusBasicResponse.builder()
                                .id(response.getPreferredBus().getId())
                                .busName(response.getPreferredBus().getBusName())
                                .busNumber(response.getPreferredBus().getBusNumber())
                                .build() : null)
                .boardingPoint(response.getBoardingPoint())
                .dropPoint(response.getDropPoint())
                .pickupTime(response.getPickupTime())
                .status(response.getStatus())
                .responseData(response.getResponseData())
                .submittedAt(response.getSubmittedAt())
                .additionalNotes(response.getAdditionalNotes())
                .build();
    }

    private BusSlotResponse mapToSlotResponse(BusSlot slot) {
        return BusSlotResponse.builder()
                .id(slot.getId())
                .slotName(slot.getSlotName())
                .pickupTime(slot.getPickupTime())
                .dropTime(slot.getDropTime())
                .fromLocation(slot.getFromLocation())
                .toLocation(slot.getToLocation())
                .status(slot.getStatus())
                .description(slot.getDescription())
                .isRegular(slot.isRegular())
                .regularDays(slot.getRegularDays())
                .createdAt(slot.getCreatedAt())
                .updatedAt(slot.getUpdatedAt())
                .build();
    }
}