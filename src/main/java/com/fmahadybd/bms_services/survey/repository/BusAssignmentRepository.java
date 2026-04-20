// com/fmahadybd/bms_services/survey/repository/BusAssignmentRepository.java
package com.fmahadybd.bms_services.survey.repository;

import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.survey.model.AssignmentStatus;
import com.fmahadybd.bms_services.survey.model.BusAssignment;
import com.fmahadybd.bms_services.survey.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusAssignmentRepository extends JpaRepository<BusAssignment, Long> {
    
    List<BusAssignment> findBySurveyId(Long surveyId);
    
    List<BusAssignment> findBySurveyAndStatus(Survey survey, AssignmentStatus status);
    
    List<BusAssignment> findByStudentIdentifier(String studentIdentifier);
    
    Optional<BusAssignment> findBySurveyIdAndStudentIdentifier(Long surveyId, String studentIdentifier);
    
    List<BusAssignment> findByAssignedRouteAndStatus(Route route, AssignmentStatus status);
    
    List<BusAssignment> findByAssignedSlotAndStatus(BusSlot slot, AssignmentStatus status);
    
    List<BusAssignment> findByAssignedBusAndStatus(Bus bus, AssignmentStatus status);
    
    long countByAssignedBusAndStatus(Bus bus, AssignmentStatus status);
    
    long countByAssignedSlotAndStatus(BusSlot slot, AssignmentStatus status);
    
    @Query("SELECT ba.assignedBus, COUNT(ba) FROM BusAssignment ba WHERE ba.survey = :survey AND ba.status = :status GROUP BY ba.assignedBus")
    List<Object[]> countAssignmentsByBus(@Param("survey") Survey survey, @Param("status") AssignmentStatus status);
    
    @Query("SELECT ba.assignedSlot, COUNT(ba) FROM BusAssignment ba WHERE ba.survey = :survey AND ba.status = :status GROUP BY ba.assignedSlot")
    List<Object[]> countAssignmentsBySlot(@Param("survey") Survey survey, @Param("status") AssignmentStatus status);
}