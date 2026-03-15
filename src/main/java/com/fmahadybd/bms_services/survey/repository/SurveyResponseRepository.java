package com.fmahadybd.bms_services.survey.repository;

import com.fmahadybd.bms_services.survey.model.ResponseStatus;
import com.fmahadybd.bms_services.survey.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    
    List<SurveyResponse> findBySurveyId(Long surveyId);
    
    List<SurveyResponse> findByStudentId(String studentId);
    
    List<SurveyResponse> findBySurveyIdAndStatus(Long surveyId, ResponseStatus status);
    
    Optional<SurveyResponse> findBySurveyIdAndStudentId(Long surveyId, String studentId);
    
    @Query("SELECT COUNT(sr) FROM SurveyResponse sr WHERE sr.survey.id = :surveyId")
    long countResponsesBySurveyId(@Param("surveyId") Long surveyId);
    
    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.survey.id = :surveyId AND sr.selectedRoute.id = :routeId")
    List<SurveyResponse> findBySurveyAndRoute(@Param("surveyId") Long surveyId, @Param("routeId") Long routeId);
    
    @Query("SELECT sr FROM SurveyResponse sr WHERE sr.survey.id = :surveyId AND sr.selectedSlot.id = :slotId")
    List<SurveyResponse> findBySurveyAndSlot(@Param("surveyId") Long surveyId, @Param("slotId") Long slotId);
    
    @Query("SELECT COUNT(sr) FROM SurveyResponse sr WHERE sr.selectedRoute.id = :routeId")
    long countByRoute(@Param("routeId") Long routeId);
    
    @Query("SELECT sr.dropPoint, COUNT(sr) FROM SurveyResponse sr WHERE sr.survey.id = :surveyId GROUP BY sr.dropPoint")
    List<Object[]> countByDropPoint(@Param("surveyId") Long surveyId);
    
    @Query("SELECT sr.boardingPoint, COUNT(sr) FROM SurveyResponse sr WHERE sr.survey.id = :surveyId GROUP BY sr.boardingPoint")
    List<Object[]> countByBoardingPoint(@Param("surveyId") Long surveyId);
    
    boolean existsBySurveyIdAndStudentId(Long surveyId, String studentId);
}