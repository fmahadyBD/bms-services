// com/fmahadybd/bms_services/survey/service/SurveyService.java
package com.fmahadybd.bms_services.survey.service;

import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import com.fmahadybd.bms_services.survey.dto.*;
import com.fmahadybd.bms_services.survey.model.Question;
import com.fmahadybd.bms_services.survey.model.Survey;
import com.fmahadybd.bms_services.survey.model.SurveyResponse;
import com.fmahadybd.bms_services.survey.repository.SurveyRepository;
import com.fmahadybd.bms_services.survey.repository.SurveyResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository responseRepository;
    private final ObjectMapper objectMapper;
    // Add to SurveyService dependencies
    private final StudentRepository studentRepository;
    private final RouteRepository routeRepository;

    // ==================== SURVEY CRUD ====================

    @Transactional
    public SurveyResponseDTO createSurvey(SurveyRequest request) {
        log.info("Creating survey: {}", request.getTitle());

        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(true)
                .build();

        List<Question> questions = new ArrayList<>();
        if (request.getQuestions() != null) {
            for (QuestionRequest q : request.getQuestions()) {
                Question question = Question.builder()
                        .survey(survey)
                        .questionText(q.getQuestionText())
                        .questionType(q.getQuestionType())
                        .options(q.getOptions())
                        .displayOrder(q.getDisplayOrder())
                        .required(q.isRequired())
                        .build();
                questions.add(question);
            }
        }
        survey.setQuestions(questions);

        Survey saved = surveyRepository.save(survey);
        return mapToDTO(saved);
    }

    @Transactional
    public SurveyResponseDTO updateSurvey(Long id, SurveyRequest request) {
        log.info("Updating survey: {}", id);

        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setStartDate(request.getStartDate());
        survey.setEndDate(request.getEndDate());

        // Remove old questions
        survey.getQuestions().clear();

        // Add new questions
        if (request.getQuestions() != null) {
            for (QuestionRequest q : request.getQuestions()) {
                Question question = Question.builder()
                        .survey(survey)
                        .questionText(q.getQuestionText())
                        .questionType(q.getQuestionType())
                        .options(q.getOptions())
                        .displayOrder(q.getDisplayOrder())
                        .required(q.isRequired())
                        .build();
                survey.getQuestions().add(question);
            }
        }

        Survey saved = surveyRepository.save(survey);
        return mapToDTO(saved);
    }

    @Transactional
    public void deleteSurvey(Long id) {
        log.info("Deleting survey: {}", id);
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        surveyRepository.delete(survey);
    }

    public List<SurveyResponseDTO> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SurveyResponseDTO getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        return mapToDTO(survey);
    }

    public List<SurveyResponseDTO> getActiveSurveys() {
        return surveyRepository.findByActiveTrue().stream()
                .filter(s -> !s.getStartDate().isAfter(LocalDateTime.now().toLocalDate()))
                .filter(s -> !s.getEndDate().isBefore(LocalDateTime.now().toLocalDate()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ==================== SUBMIT RESPONSE ====================

    @Transactional
    public void submitResponse(Long surveyId, SubmissionRequest request) {
        log.info("Submitting response for survey: {} from student: {}", surveyId, request.getStudentId());

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        // Check if already submitted
        if (responseRepository.existsBySurveyIdAndStudentId(surveyId, request.getStudentId())) {
            throw new IllegalStateException("Student already submitted this survey");
        }

        // Convert answers to JSON
        String answersJson;
        try {
            answersJson = objectMapper.writeValueAsString(request.getAnswers());
        } catch (Exception e) {
            answersJson = "{}";
        }

        SurveyResponse response = SurveyResponse.builder()
                .survey(survey)
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .studentEmail(request.getStudentEmail())
                .studentPhone(request.getStudentPhone())
                .selectedRouteId(request.getSelectedRouteId())
                .selectedSlotId(request.getSelectedSlotId())
                .responseData(answersJson)
                .submittedAt(LocalDateTime.now())
                .build();

        responseRepository.save(response);

        // ── Auto-assign route to student after survey submission ──────────
        if (request.getSelectedRouteId() != null && request.getStudentId() != null) {
            try {
                Student student = studentRepository.findByStudentId(request.getStudentId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Student not found: " + request.getStudentId()));

                Route route = routeRepository.findById(request.getSelectedRouteId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Route not found: " + request.getSelectedRouteId()));

                student.setRoute(route);
                studentRepository.save(student);

                log.info("Route {} assigned to student {} after survey submission",
                        request.getSelectedRouteId(), request.getStudentId());

            } catch (ResourceNotFoundException e) {
                // Log but don't fail the survey submission if route/student lookup fails
                log.warn("Could not assign route after survey: {}", e.getMessage());
            }
        }
    }

    public List<SurveyResponse> getSurveyResponses(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }

    // ==================== MAPPER ====================

    private SurveyResponseDTO mapToDTO(Survey survey) {
        List<QuestionResponseDTO> questions = null;
        if (survey.getQuestions() != null) {
            questions = survey.getQuestions().stream()
                    .map(q -> QuestionResponseDTO.builder()
                            .id(q.getId())
                            .questionText(q.getQuestionText())
                            .questionType(q.getQuestionType())
                            .options(q.getOptions())
                            .displayOrder(q.getDisplayOrder())
                            .required(q.isRequired())
                            .build())
                    .collect(Collectors.toList());
        }

        return SurveyResponseDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .active(survey.isActive())
                .questions(questions)
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .build();
    }
}