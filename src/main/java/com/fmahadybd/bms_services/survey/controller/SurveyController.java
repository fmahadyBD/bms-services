// com/fmahadybd/bms_services/survey/controller/SurveyController.java
package com.fmahadybd.bms_services.survey.controller;

import com.fmahadybd.bms_services.survey.dto.SurveyRequest;
import com.fmahadybd.bms_services.survey.dto.SurveyResponseDTO;
import com.fmahadybd.bms_services.survey.dto.SubmissionRequest;
import com.fmahadybd.bms_services.survey.model.SurveyResponse;
import com.fmahadybd.bms_services.survey.repository.SurveyResponseRepository;
import com.fmahadybd.bms_services.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyResponseRepository surveyResponseRepository;

    @GetMapping("/all-responses")
    public ResponseEntity<List<SurveyResponse>> getAllResponses() {
        return ResponseEntity.ok(surveyResponseRepository.findAll());
    }

    // Manager endpoints
    @PostMapping
    public ResponseEntity<SurveyResponseDTO> createSurvey(@RequestBody SurveyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyService.createSurvey(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponseDTO> updateSurvey(@PathVariable Long id, @RequestBody SurveyRequest request) {
        return ResponseEntity.ok(surveyService.updateSurvey(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SurveyResponseDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponseDTO> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    // Student endpoints
    @GetMapping("/active")
    public ResponseEntity<List<SurveyResponseDTO>> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveys());
    }

    @PostMapping("/{surveyId}/submit")
    public ResponseEntity<Void> submitResponse(@PathVariable Long surveyId, @RequestBody SubmissionRequest request) {
        surveyService.submitResponse(surveyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{surveyId}/responses")
    public ResponseEntity<List<SurveyResponse>> getResponses(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyResponses(surveyId));
    }
}