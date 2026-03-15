package com.fmahadybd.bms_services.survey.repository;

import com.fmahadybd.bms_services.survey.model.Survey;
import com.fmahadybd.bms_services.survey.model.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    
    List<Survey> findByStatus(SurveyStatus status);
    
    List<Survey> findByIsActiveTrue();
    
    List<Survey> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date1, LocalDate date2);
    
    @Query("SELECT s FROM Survey s WHERE s.status = :status AND s.isActive = true")
    List<Survey> findActiveByStatus(@Param("status") SurveyStatus status);
    
    @Query("SELECT s FROM Survey s WHERE s.academicYear = :academicYear AND s.semester = :semester")
    List<Survey> findByAcademicYearAndSemester(@Param("academicYear") String academicYear, 
                                                @Param("semester") String semester);
    
    @Query("SELECT COUNT(r) FROM SurveyResponse r WHERE r.survey.id = :surveyId")
    long countResponsesBySurveyId(@Param("surveyId") Long surveyId);
}