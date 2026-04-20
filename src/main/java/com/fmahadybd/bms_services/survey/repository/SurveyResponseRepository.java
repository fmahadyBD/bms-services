// com/fmahadybd/bms_services/survey/repository/SurveyResponseRepository.java
package com.fmahadybd.bms_services.survey.repository;

import com.fmahadybd.bms_services.survey.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findBySurveyId(Long surveyId);
    Optional<SurveyResponse> findBySurveyIdAndStudentId(Long surveyId, String studentId);
    boolean existsBySurveyIdAndStudentId(Long surveyId, String studentId);
}