// com/fmahadybd/bms_services/survey/repository/SurveyRepository.java
package com.fmahadybd.bms_services.survey.repository;

import com.fmahadybd.bms_services.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByActiveTrue();
    List<Survey> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndActiveTrue(LocalDate date1, LocalDate date2);
}