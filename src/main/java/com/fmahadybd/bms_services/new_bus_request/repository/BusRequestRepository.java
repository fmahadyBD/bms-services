package com.fmahadybd.bms_services.new_bus_request.repository;


import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import com.fmahadybd.bms_services.new_bus_request.model.BusRequest;
import com.fmahadybd.bms_services.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusRequestRepository extends JpaRepository<BusRequest, Long> {
    
    // Find by student
    List<BusRequest> findByStudent(Student student);
    
    // Find by student ID
    List<BusRequest> findByStudentId(Long studentId);
    
    // Find by status
    List<BusRequest> findByStatus(BUS_REQUEST_STATUS status);
    
    // Find by route
    List<BusRequest> findByRouteId(Long routeId);
    
    // Find by pickup point
    List<BusRequest> findByPickupPointId(Long pickupPointId);
    
    // Find by date range
    List<BusRequest> findByRequestedFromLessThanEqualAndRequestedToGreaterThanEqual(LocalDate date1, LocalDate date2);
    
    // Check if student has active request for same route and date range
    boolean existsByStudentIdAndRouteIdAndStatusAndRequestedFromLessThanEqualAndRequestedToGreaterThanEqual(
        Long studentId, Long routeId, BUS_REQUEST_STATUS status, LocalDate date1, LocalDate date2);
    
    // Find active requests (approved and within date range)
    @Query("SELECT br FROM BusRequest br WHERE br.status = 'APPROVED' AND :currentDate BETWEEN br.requestedFrom AND br.requestedTo")
    List<BusRequest> findActiveRequests(@Param("currentDate") LocalDate currentDate);
    
    // Find pending requests
    List<BusRequest> findByStatusOrderByCreatedAtDesc(BUS_REQUEST_STATUS status);
    
    // Count by status
    long countByStatus(BUS_REQUEST_STATUS status);
    
    // Find requests by student with pagination (implicitly via method name)
    List<BusRequest> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    
    // Find expired requests (where requestedTo is before current date)
    @Query("SELECT br FROM BusRequest br WHERE br.requestedTo < :currentDate AND br.status = 'APPROVED'")
    List<BusRequest> findExpiredRequests(@Param("currentDate") LocalDate currentDate);
    
    // Find requests by multiple statuses
    @Query("SELECT br FROM BusRequest br WHERE br.status IN :statuses")
    List<BusRequest> findByStatusIn(@Param("statuses") List<BUS_REQUEST_STATUS> statuses);
}