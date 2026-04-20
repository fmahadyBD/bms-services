// com/fmahadybd/bms_services/survey/service/SurveyServiceEnhanced.java - COMPLETELY FIXED
package com.fmahadybd.bms_services.survey.service;

import com.fmahadybd.bms_services.bus.dto.BusBasicResponse;
import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.bus.repository.BusRepository;
import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.dto.RouteBasicResponse;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.slot.dto.BusSlotResponse;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.slot.repository.BusSlotRepository;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import com.fmahadybd.bms_services.survey.dto.*;
import com.fmahadybd.bms_services.survey.model.*;
import com.fmahadybd.bms_services.survey.repository.BusAssignmentRepository;
import com.fmahadybd.bms_services.survey.repository.SurveyRepository;
import com.fmahadybd.bms_services.survey.repository.SurveyResponseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceEnhanced {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository responseRepository;
    private final BusAssignmentRepository assignmentRepository;
    private final RouteRepository routeRepository;
    private final BusSlotRepository busSlotRepository;
    private final BusRepository busRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;

    // ─────────────────────────────────────────────────
    // SURVEY CREATION WITH DEFAULTS
    // ─────────────────────────────────────────────────

    @Transactional
    public SurveyDetailResponse createSurveyWithDefaults(SurveyWithDefaultsRequest request, Long userId) {
        log.info("Creating survey with transport defaults: {}", request.getTitle());

        // Create base survey
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

        // Add default transport questions
        List<SurveyQuestion> questions = new ArrayList<>();
        
        // Route question (default)
        questions.add(createDefaultQuestion(survey, "Select your preferred route", 
                QuestionType.SINGLE_CHOICE, "route", 1, true));
        
        // Slot question (default)
        questions.add(createDefaultQuestion(survey, "Select your preferred time slot", 
                QuestionType.SINGLE_CHOICE, "slot", 2, true));
        
        // Day question (default)
        questions.add(createDefaultQuestion(survey, "Select your travel days", 
                QuestionType.MULTIPLE_CHOICE, "day", 3, true));
        
        // Boarding point question (default)
        questions.add(createDefaultQuestion(survey, "Select your boarding point", 
                QuestionType.SINGLE_CHOICE, "boardingPoint", 4, true));
        
        // Drop point question (default)
        questions.add(createDefaultQuestion(survey, "Select your drop point", 
                QuestionType.SINGLE_CHOICE, "dropPoint", 5, true));

        // Add custom questions if any
        if (request.getCustomQuestions() != null) {
            int order = 6;
            for (SurveyQuestionRequest q : request.getCustomQuestions()) {
                questions.add(SurveyQuestion.builder()
                        .survey(survey)
                        .questionText(q.getQuestionText())
                        .questionType(q.getQuestionType())
                        .options(q.getOptions())
                        .displayOrder(order++)
                        .required(q.isRequired())
                        .isActive(true)
                        .build());
            }
        }
        
        survey.setQuestions(questions);

        // Add available routes, slots, and days
        if (request.getTransportDefaults() != null) {
            SurveyTransportDefaults defaults = request.getTransportDefaults();
            
            if (defaults.getAvailableRouteIds() != null && !defaults.getAvailableRouteIds().isEmpty()) {
                List<Route> routes = routeRepository.findAllById(defaults.getAvailableRouteIds());
                survey.setAvailableRoutes(routes);
            }
            
            if (defaults.getAvailableSlotIds() != null && !defaults.getAvailableSlotIds().isEmpty()) {
                List<BusSlot> slots = busSlotRepository.findAllById(defaults.getAvailableSlotIds());
                survey.setAvailableSlots(slots);
            }
            
            // Store transport configuration in metadata
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("availableDays", defaults.getAvailableDays());
            metadata.put("busCapacityLimit", defaults.getBusCapacityLimit());
            metadata.put("autoAssignBuses", defaults.isAutoAssignBuses());
            survey.setMetadata(metadata);
        }

        Survey savedSurvey = surveyRepository.save(survey);
        return mapToSurveyDetailResponse(savedSurvey);
    }

    private SurveyQuestion createDefaultQuestion(Survey survey, String text, 
            QuestionType type, String category, int order, boolean required) {
        SurveyQuestion question = SurveyQuestion.builder()
                .survey(survey)
                .questionText(text)
                .questionType(type)
                .displayOrder(order)
                .required(required)
                .isActive(true)
                .build();
        
        // Add metadata for special handling
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", category);
        question.setMetadata(metadata);
        
        return question;
    }

    // ─────────────────────────────────────────────────
    // BUS ASSIGNMENT LOGIC WITH CAPACITY MANAGEMENT
    // ─────────────────────────────────────────────────

    @Transactional
    public AssignmentResult assignStudentToTransport(Long surveyId, String studentId, 
            TransportPreference preference, Long managerId) {
        
        log.info("Assigning student {} to transport for survey {}", studentId, surveyId);
        
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        Student student = studentOpt.orElse(null);
        
        Route requestedRoute = routeRepository.findById(preference.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
        
        BusSlot requestedSlot = busSlotRepository.findById(preference.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        
        // Get capacity limit from survey metadata
        Integer capacityLimit = getCapacityLimit(survey);
        
        // Check existing assignments for this slot
        long currentSlotAssignments = assignmentRepository.countByAssignedSlotAndStatus(
                requestedSlot, AssignmentStatus.APPROVED);
        
        // Check if we need a new bus
        List<Bus> availableBuses = getAvailableBusesForRoute(requestedRoute);
        Bus assignedBus = null;
        AssignmentStatus status;
        String message;
        
        if (currentSlotAssignments >= capacityLimit) {
            // Slot is full, try to add a new bus
            if (!availableBuses.isEmpty()) {
                assignedBus = availableBuses.get(0);
                status = AssignmentStatus.ASSIGNED;
                message = "Assigned to existing bus due to capacity";
                
                // Update slot to reflect new bus
                requestedSlot.setBus(assignedBus);
                busSlotRepository.save(requestedSlot);
            } else {
                status = AssignmentStatus.WAITLISTED;
                message = "No available buses, added to waitlist";
            }
        } else {
            // Try to assign to existing bus for this slot
            assignedBus = requestedSlot.getBus();
            if (assignedBus == null && !availableBuses.isEmpty()) {
                assignedBus = availableBuses.get(0);
                requestedSlot.setBus(assignedBus);
                busSlotRepository.save(requestedSlot);
            }
            status = AssignmentStatus.APPROVED;
            message = "Successfully assigned";
        }
        
        // Create assignment
        BusAssignment.BusAssignmentBuilder assignmentBuilder = BusAssignment.builder()
                .survey(survey)
                .studentIdentifier(studentId)
                .assignedRoute(requestedRoute)
                .assignedSlot(requestedSlot)
                .assignedBus(assignedBus)
                .boardingPoint(preference.getBoardingPoint())
                .dropPoint(preference.getDropPoint())
                .pickupTime(preference.getPickupTime())
                .status(status)
                .assignedBy(managerId)
                .assignedAt(LocalDateTime.now());
        
        if (student != null) {
            assignmentBuilder.student(student);
        }
        
        if (preference.getPreferredDay() != null) {
            try {
                assignmentBuilder.assignedDay(DAY.valueOf(preference.getPreferredDay().toUpperCase()));
            } catch (IllegalArgumentException e) {
                assignmentBuilder.assignedDay(DAY.MONDAY);
            }
        }
        
        BusAssignment assignment = assignmentBuilder.build();
        assignmentRepository.save(assignment);
        
        // Update survey response if exists
        updateSurveyResponseWithAssignment(surveyId, studentId, assignment);
        
        return AssignmentResult.builder()
                .assignmentId(assignment.getId())
                .assignedBus(assignedBus)
                .status(status)
                .message(message)
                .build();
    }
    
    private Integer getCapacityLimit(Survey survey) {
        Map<String, Object> metadata = survey.getMetadata();
        if (metadata != null && metadata.containsKey("busCapacityLimit")) {
            Object limit = metadata.get("busCapacityLimit");
            if (limit instanceof Integer) {
                return (Integer) limit;
            } else if (limit instanceof String) {
                try {
                    return Integer.parseInt((String) limit);
                } catch (NumberFormatException e) {
                    return 40;
                }
            }
        }
        return 40;
    }
    
    private List<Bus> getAvailableBusesForRoute(Route route) {
        return busRepository.findByRouteIdAndStatus(route.getId(), BUS_STATUS.ACTIVE);
    }
    
    private void updateSurveyResponseWithAssignment(Long surveyId, String studentId, BusAssignment assignment) {
        Optional<SurveyResponse> responseOpt = responseRepository.findBySurveyIdAndStudentId(surveyId, studentId);
        if (responseOpt.isPresent()) {
            SurveyResponse response = responseOpt.get();
            try {
                Map<String, Object> responseData = parseResponseData(response.getResponseData());
                responseData.put("assignmentId", assignment.getId());
                responseData.put("assignmentStatus", assignment.getStatus().toString());
                response.setResponseData(objectMapper.writeValueAsString(responseData));
                responseRepository.save(response);
            } catch (JsonProcessingException e) {
                log.error("Failed to update response data", e);
            }
        }
    }
    
    private Map<String, Object> parseResponseData(String responseDataJson) {
        if (responseDataJson == null || responseDataJson.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(responseDataJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to parse response data", e);
            return new HashMap<>();
        }
    }

    // ─────────────────────────────────────────────────
    // MANAGER DASHBOARD
    // ─────────────────────────────────────────────────

    public ManagerDashboardDTO getManagerDashboard(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        
        List<SurveyResponse> responses = responseRepository.findBySurveyId(surveyId);
        List<BusAssignment> assignments = assignmentRepository.findBySurveyId(surveyId);
        
        long approvedAssignments = assignments.stream().filter(a -> a.getStatus() == AssignmentStatus.APPROVED).count();
        long totalResponses = responses.size();
        
        // Calculate stats
        DashboardStats stats = DashboardStats.builder()
                .totalResponses(totalResponses)
                .approvedAssignments(approvedAssignments)
                .pendingAssignments(assignments.stream().filter(a -> a.getStatus() == AssignmentStatus.PENDING).count())
                .waitlistedAssignments(assignments.stream().filter(a -> a.getStatus() == AssignmentStatus.WAITLISTED).count())
                .totalBusesUsed(assignments.stream().map(BusAssignment::getAssignedBus).filter(Objects::nonNull).distinct().count())
                .overallCapacityUtilization(totalResponses > 0 ? (double) approvedAssignments / totalResponses * 100 : 0)
                .build();
        
        // Slot assignments view
        List<SlotAssignmentView> slotAssignments = getSlotAssignmentsView(survey, assignments);
        
        // Bus utilization view
        List<BusUtilizationView> busUtilizations = getBusUtilizationsView(survey, assignments);
        
        // Pending assignments
        List<PendingAssignmentView> pendingAssignments = getPendingAssignmentsView(responses, assignments);
        
        return ManagerDashboardDTO.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .stats(stats)
                .slotAssignments(slotAssignments)
                .busUtilizations(busUtilizations)
                .pendingAssignments(pendingAssignments)
                .build();
    }
    
    private List<SlotAssignmentView> getSlotAssignmentsView(Survey survey, List<BusAssignment> assignments) {
        Integer capacityLimit = getCapacityLimit(survey);
        
        if (survey.getAvailableSlots() == null || survey.getAvailableSlots().isEmpty()) {
            return new ArrayList<>();
        }
        
        return survey.getAvailableSlots().stream()
                .map(slot -> {
                    List<BusAssignment> slotAssignments = assignments.stream()
                            .filter(a -> a.getAssignedSlot() != null && 
                                        a.getAssignedSlot().getId().equals(slot.getId()) &&
                                        a.getStatus() == AssignmentStatus.APPROVED)
                            .collect(Collectors.toList());
                    
                    long totalAssigned = slotAssignments.size();
                    long availableSeats = Math.max(0, capacityLimit - totalAssigned);
                    
                    // Group by bus for this slot
                    Map<Bus, List<BusAssignment>> busGroups = slotAssignments.stream()
                            .filter(a -> a.getAssignedBus() != null)
                            .collect(Collectors.groupingBy(BusAssignment::getAssignedBus));
                    
                    List<BusAssignmentSummary> assignedBuses = busGroups.entrySet().stream()
                            .map(entry -> BusAssignmentSummary.builder()
                                    .busId(entry.getKey().getId())
                                    .busNumber(entry.getKey().getBusNumber())
                                    .assignedCount(entry.getValue().size())
                                    .capacity(capacityLimit)
                                    .build())
                            .collect(Collectors.toList());
                    
                    double utilization = capacityLimit > 0 ? totalAssigned * 100.0 / capacityLimit : 0;
                    
                    return SlotAssignmentView.builder()
                            .slot(mapToSlotResponse(slot))
                            .route(slot.getRoute() != null ? mapToRouteBasicResponse(slot.getRoute()) : null)
                            .totalAssigned(totalAssigned)
                            .capacityLimit(capacityLimit)
                            .availableSeats(availableSeats)
                            .utilizationPercentage(utilization)
                            .assignedBuses(assignedBuses)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private List<BusUtilizationView> getBusUtilizationsView(Survey survey, List<BusAssignment> assignments) {
        Integer capacityLimit = getCapacityLimit(survey);
        
        Map<Bus, List<BusAssignment>> busAssignments = assignments.stream()
                .filter(a -> a.getAssignedBus() != null && a.getStatus() == AssignmentStatus.APPROVED)
                .collect(Collectors.groupingBy(BusAssignment::getAssignedBus));
        
        return busAssignments.entrySet().stream()
                .map(entry -> {
                    Bus bus = entry.getKey();
                    List<BusAssignment> busAssigns = entry.getValue();
                    long assignedCount = busAssigns.size();
                    
                    BusSlot assignedSlot = busAssigns.stream()
                            .findFirst()
                            .map(BusAssignment::getAssignedSlot)
                            .orElse(null);
                    
                    double utilization = capacityLimit > 0 ? assignedCount * 100.0 / capacityLimit : 0;
                    
                    return BusUtilizationView.builder()
                            .bus(mapToBusBasicResponse(bus))
                            .route(assignedSlot != null && assignedSlot.getRoute() != null ? 
                                    mapToRouteBasicResponse(assignedSlot.getRoute()) : null)
                            .slot(assignedSlot != null ? mapToSlotResponse(assignedSlot) : null)
                            .assignedStudents(assignedCount)
                            .capacity(capacityLimit)
                            .utilizationPercentage(utilization)
                            .status(assignedCount >= capacityLimit ? "FULL" : "AVAILABLE")
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private List<PendingAssignmentView> getPendingAssignmentsView(List<SurveyResponse> responses, 
            List<BusAssignment> assignments) {
        
        Set<String> assignedStudentIds = assignments.stream()
                .map(BusAssignment::getStudentIdentifier)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        List<SurveyResponse> pendingResponses = responses.stream()
                .filter(r -> !assignedStudentIds.contains(r.getStudentId()))
                .limit(100)
                .collect(Collectors.toList());
        
        return pendingResponses.stream()
                .map(response -> {
                    Map<String, Object> responseData = parseResponseData(response.getResponseData());
                    return PendingAssignmentView.builder()
                            .assignmentId(null)
                            .studentId(response.getStudentId())
                            .studentName(response.getStudentName())
                            .studentEmail(response.getStudentEmail())
                            .requestedRoute(responseData != null && responseData.containsKey("selectedRoute") ? 
                                    responseData.get("selectedRoute").toString() : "Not specified")
                            .requestedSlot(responseData != null && responseData.containsKey("selectedSlot") ? 
                                    responseData.get("selectedSlot").toString() : "Not specified")
                            .requestedDay(responseData != null && responseData.containsKey("selectedDay") ? 
                                    responseData.get("selectedDay").toString() : "Not specified")
                            .submittedAt(response.getSubmittedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────
    // STUDENT TRANSPORT DETAILS
    // ─────────────────────────────────────────────────

    public StudentTransportDetailsDTO getStudentTransportDetails(String studentId, Long surveyId) {
        BusAssignment assignment = assignmentRepository
                .findBySurveyIdAndStudentIdentifier(surveyId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("No transport assignment found"));
        
        return buildStudentTransportDetails(assignment);
    }
    
    public List<StudentTransportDetailsDTO> getAllStudentTransports(String studentId) {
        List<BusAssignment> assignments = assignmentRepository.findByStudentIdentifier(studentId);
        
        return assignments.stream()
                .map(this::buildStudentTransportDetails)
                .collect(Collectors.toList());
    }
    
    private StudentTransportDetailsDTO buildStudentTransportDetails(BusAssignment assignment) {
        List<DailySchedule> weeklySchedule = new ArrayList<>();
        
        if (assignment.getAssignedDay() != null) {
            String dropTime = assignment.getAssignedSlot() != null && assignment.getAssignedSlot().getDropTime() != null ? 
                    assignment.getAssignedSlot().getDropTime().toString() : "TBD";
            String busNumber = assignment.getAssignedBus() != null ? 
                    assignment.getAssignedBus().getBusNumber() : "TBD";
            String driverContact = assignment.getAssignedBus() != null && assignment.getAssignedBus().getDriverPhone() != null ? 
                    assignment.getAssignedBus().getDriverPhone() : "N/A";
            
            weeklySchedule.add(DailySchedule.builder()
                    .day(assignment.getAssignedDay().toString())
                    .pickupTime(assignment.getPickupTime() != null ? assignment.getPickupTime() : "TBD")
                    .dropTime(dropTime)
                    .busNumber(busNumber)
                    .driverContact(driverContact)
                    .build());
        }
        
        LocalDateTime validFrom = assignment.getSurvey().getStartDate() != null ? 
                assignment.getSurvey().getStartDate().atStartOfDay() : LocalDateTime.now();
        LocalDateTime validUntil = assignment.getSurvey().getEndDate() != null ? 
                assignment.getSurvey().getEndDate().atTime(23, 59, 59) : LocalDateTime.now().plusDays(30);
        
        return StudentTransportDetailsDTO.builder()
                .assignmentId(assignment.getId())
                .surveyId(assignment.getSurvey().getId())
                .surveyTitle(assignment.getSurvey().getTitle())
                .route(assignment.getAssignedRoute() != null ? 
                        mapToRouteBasicResponse(assignment.getAssignedRoute()) : null)
                .slot(assignment.getAssignedSlot() != null ? 
                        mapToSlotResponse(assignment.getAssignedSlot()) : null)
                .bus(assignment.getAssignedBus() != null ? 
                        mapToBusBasicResponse(assignment.getAssignedBus()) : null)
                .assignedDay(assignment.getAssignedDay() != null ? 
                        assignment.getAssignedDay().toString() : null)
                .boardingPoint(assignment.getBoardingPoint())
                .dropPoint(assignment.getDropPoint())
                .pickupTime(assignment.getPickupTime())
                .status(assignment.getStatus())
                .assignedAt(assignment.getAssignedAt())
                .validFrom(validFrom)
                .validUntil(validUntil)
                .weeklySchedule(weeklySchedule)
                .build();
    }

    // ─────────────────────────────────────────────────
    // BATCH ASSIGNMENT FOR ALL SURVEY RESPONSES
    // ─────────────────────────────────────────────────

    @Transactional
    public BatchAssignmentResult batchAssignStudents(Long surveyId, Long managerId) {
        log.info("Batch assigning students for survey: {}", surveyId);
        
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        
        List<SurveyResponse> responses = responseRepository.findBySurveyId(surveyId);
        List<BusAssignment> existingAssignments = assignmentRepository.findBySurveyId(surveyId);
        
        Set<String> alreadyAssigned = existingAssignments.stream()
                .map(BusAssignment::getStudentIdentifier)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        List<SurveyResponse> unassignedResponses = responses.stream()
                .filter(r -> !alreadyAssigned.contains(r.getStudentId()))
                .collect(Collectors.toList());
        
        int assigned = 0;
        int failed = 0;
        List<AssignmentResult> results = new ArrayList<>();
        
        for (SurveyResponse response : unassignedResponses) {
            try {
                TransportPreference preference = extractPreferenceFromResponse(response);
                AssignmentResult result = assignStudentToTransport(
                        surveyId, response.getStudentId(), preference, managerId);
                results.add(result);
                if (result.getStatus() != AssignmentStatus.WAITLISTED) {
                    assigned++;
                } else {
                    failed++;
                }
            } catch (Exception e) {
                log.error("Failed to assign student {}: {}", response.getStudentId(), e.getMessage());
                failed++;
            }
        }
        
        return BatchAssignmentResult.builder()
                .totalProcessed(unassignedResponses.size())
                .successfullyAssigned(assigned)
                .waitlisted(failed)
                .results(results)
                .build();
    }
    
    private TransportPreference extractPreferenceFromResponse(SurveyResponse response) {
        Map<String, Object> data = parseResponseData(response.getResponseData());
        
        // Try to get from responseData first
        Long routeId = null;
        if (data.containsKey("selectedRouteId")) {
            Object routeIdObj = data.get("selectedRouteId");
            if (routeIdObj instanceof Number) {
                routeId = ((Number) routeIdObj).longValue();
            } else if (routeIdObj instanceof String) {
                try {
                    routeId = Long.valueOf((String) routeIdObj);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }
        
        if (routeId == null && response.getSelectedRoute() != null) {
            routeId = response.getSelectedRoute().getId();
        }
        
        Long slotId = null;
        if (data.containsKey("selectedSlotId")) {
            Object slotIdObj = data.get("selectedSlotId");
            if (slotIdObj instanceof Number) {
                slotId = ((Number) slotIdObj).longValue();
            } else if (slotIdObj instanceof String) {
                try {
                    slotId = Long.valueOf((String) slotIdObj);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }
        
        if (slotId == null && response.getSelectedSlot() != null) {
            slotId = response.getSelectedSlot().getId();
        }
        
        String preferredDay = "MONDAY";
        if (data.containsKey("selectedDay")) {
            preferredDay = data.get("selectedDay").toString();
        }
        
        return TransportPreference.builder()
                .routeId(routeId)
                .slotId(slotId)
                .preferredDay(preferredDay)
                .boardingPoint(response.getBoardingPoint())
                .dropPoint(response.getDropPoint())
                .pickupTime(response.getPickupTime())
                .build();
    }

    // ─────────────────────────────────────────────────
    // HELPER METHODS
    // ─────────────────────────────────────────────────

    private SurveyDetailResponse mapToSurveyDetailResponse(Survey survey) {
        return SurveyDetailResponse.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .status(survey.getStatus())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .totalResponses(survey.getTotalResponses())
                .isActive(survey.isActive())
                .build();
    }
    
    private RouteBasicResponse mapToRouteBasicResponse(Route route) {
        if (route == null) return null;
        return RouteBasicResponse.builder()
                .id(route.getId())
                .routeName(route.getRouteName())
                .busNo(route.getBusNo())
                .startPoint(route.getStartPoint())
                .endPoint(route.getEndPoint())
                .build();
    }
    
    private BusSlotResponse mapToSlotResponse(BusSlot slot) {
        if (slot == null) return null;
        return BusSlotResponse.builder()
                .id(slot.getId())
                .slotName(slot.getSlotName())
                .pickupTime(slot.getPickupTime())
                .dropTime(slot.getDropTime())
                .fromLocation(slot.getFromLocation())
                .toLocation(slot.getToLocation())
                .status(slot.getStatus())
                .build();
    }
    
    private BusBasicResponse mapToBusBasicResponse(Bus bus) {
        if (bus == null) return null;
        return BusBasicResponse.builder()
                .id(bus.getId())
                .busName(bus.getBusName())
                .busNumber(bus.getBusNumber())
                .build();
    }
}