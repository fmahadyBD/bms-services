// com/fmahadybd/bms_services/survey/controller/SurveyController.java - Fixed
package com.fmahadybd.bms_services.survey.controller;

import com.fmahadybd.bms_services.survey.dto.*;
import com.fmahadybd.bms_services.survey.model.ResponseStatus;
import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import com.fmahadybd.bms_services.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // ─────────────────────────────────────────────────
    // SURVEY MANAGEMENT ENDPOINTS
    // ─────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<SurveyDetailResponse> createSurvey(
            @Valid @RequestBody SurveyRequest request) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surveyService.createSurvey(request, managerId));
    }

    @PostMapping("/with-defaults")
    public ResponseEntity<SurveyDetailResponse> createSurveyWithDefaults(
            @Valid @RequestBody SurveyWithDefaultsRequest request) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surveyService.createSurveyWithDefaults(request, managerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDetailResponse> updateSurvey(
            @PathVariable Long id,
            @Valid @RequestBody SurveyRequest request) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(surveyService.updateSurvey(id, request, managerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SurveyDetailResponse> updateSurveyStatus(
            @PathVariable Long id,
            @RequestParam SurveyStatus status) {
        Long managerId = 1L; // This should come from security context
        return ResponseEntity.ok(surveyService.updateSurveyStatus(id, status, managerId));
    }

    @GetMapping
    public ResponseEntity<List<SurveySummaryResponse>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SurveyDetailResponse>> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveys());
    }

    @GetMapping("/current")
    public ResponseEntity<List<SurveySummaryResponse>> getCurrentSurveys() {
        return ResponseEntity.ok(surveyService.getCurrentSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDetailResponse> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────
    // SURVEY RESPONSE ENDPOINTS
    // ─────────────────────────────────────────────────

    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<SurveySubmissionResponse> submitResponse(
            @PathVariable Long surveyId,
            @Valid @RequestBody SurveySubmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surveyService.submitResponse(surveyId, request));
    }

    @PatchMapping("/responses/{responseId}/status")
    public ResponseEntity<SurveySubmissionResponse> updateResponseStatus(
            @PathVariable Long responseId,
            @RequestParam ResponseStatus status,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(surveyService.updateResponseStatus(responseId, status, reason));
    }

    @GetMapping("/{surveyId}/responses")
    public ResponseEntity<List<SurveySubmissionResponse>> getResponsesBySurvey(
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getResponsesBySurvey(surveyId));
    }

    @GetMapping("/{surveyId}/responses/student/{studentId}")
    public ResponseEntity<SurveySubmissionResponse> getStudentResponse(
            @PathVariable Long surveyId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(surveyService.getStudentResponse(surveyId, studentId));
    }

    @GetMapping("/responses/student/{studentId}")
    public ResponseEntity<List<SurveySubmissionResponse>> getResponsesByStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(surveyService.getResponsesByStudent(studentId));
    }

    @GetMapping("/{surveyId}/responses/route/{routeId}")
    public ResponseEntity<List<SurveySubmissionResponse>> getResponsesByRoute(
            @PathVariable Long surveyId,
            @PathVariable Long routeId) {
        return ResponseEntity.ok(surveyService.getResponsesByRoute(surveyId, routeId));
    }

    @GetMapping("/{surveyId}/responses/slot/{slotId}")
    public ResponseEntity<List<SurveySubmissionResponse>> getResponsesBySlot(
            @PathVariable Long surveyId,
            @PathVariable Long slotId) {
        return ResponseEntity.ok(surveyService.getResponsesBySlot(surveyId, slotId));
    }

    // ─────────────────────────────────────────────────
    // ASSIGNMENT ENDPOINTS
    // ─────────────────────────────────────────────────

    @PostMapping("/{surveyId}/assign/{studentId}")
    public ResponseEntity<AssignmentResult> assignStudentToTransport(
            @PathVariable Long surveyId,
            @PathVariable String studentId,
            @RequestBody TransportPreference preference) {
        Long managerId = 1L;
        return ResponseEntity.ok(surveyService.assignStudentToTransport(surveyId, studentId, preference, managerId));
    }

    @PostMapping("/{surveyId}/batch-assign")
    public ResponseEntity<BatchAssignmentResult> batchAssignStudents(@PathVariable Long surveyId) {
        Long managerId = 1L;
        return ResponseEntity.ok(surveyService.batchAssignStudents(surveyId, managerId));
    }

    // ─────────────────────────────────────────────────
    // MANAGER DASHBOARD ENDPOINTS
    // ─────────────────────────────────────────────────

    @GetMapping("/{surveyId}/dashboard")
    public ResponseEntity<ManagerDashboardDTO> getManagerDashboard(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getManagerDashboard(surveyId));
    }

    // ─────────────────────────────────────────────────
    // STUDENT TRANSPORT ENDPOINTS
    // ─────────────────────────────────────────────────

    @GetMapping("/transport/student/{studentId}")
    public ResponseEntity<List<StudentTransportDetailsDTO>> getAllStudentTransports(
            @PathVariable String studentId) {
        return ResponseEntity.ok(surveyService.getAllStudentTransports(studentId));
    }

    @GetMapping("/{surveyId}/transport/student/{studentId}")
    public ResponseEntity<StudentTransportDetailsDTO> getStudentTransportDetails(
            @PathVariable Long surveyId,
            @PathVariable String studentId) {
        return ResponseEntity.ok(surveyService.getStudentTransportDetails(studentId, surveyId));
    }

    // ─────────────────────────────────────────────────
    // STATISTICS ENDPOINTS
    // ─────────────────────────────────────────────────

    @GetMapping("/{id}/statistics")
    public ResponseEntity<SurveyStatistics> getSurveyStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyStatistics(id));
    }

    // ─────────────────────────────────────────────────
    // EXPORT ENDPOINTS
    // ─────────────────────────────────────────────────

    @GetMapping("/{surveyId}/export")
    public ResponseEntity<List<Map<String, Object>>> exportSurveyResponses(
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.exportSurveyResponses(surveyId));
    }
}