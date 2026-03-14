package com.fmahadybd.bms_services.slot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.slot.model.BusSlot;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BusSlotRepository extends JpaRepository<BusSlot, Long> {

    List<BusSlot> findByRouteId(Long routeId);
    
    List<BusSlot> findByPickupPointId(Long pickupPointId);
    
    List<BusSlot> findByStatus(BUS_SLOT_STATUS status);
    
    List<BusSlot> findByRouteIdAndStatus(Long routeId, BUS_SLOT_STATUS status);
    
    @Query("SELECT b FROM BusSlot b WHERE b.status = 'ACTIVE' AND b.currentBookings < b.maxCapacity")
    List<BusSlot> findAvailableSlots();
    
    List<BusSlot> findByPickupTimeBetween(LocalTime startTime, LocalTime endTime);
    
    List<BusSlot> findByIsRecurringTrue();
    
    List<BusSlot> findByRouteIdAndPickupTimeBetween(Long routeId, LocalTime startTime, LocalTime endTime);
    
    boolean existsByRouteIdAndPickupTime(Long routeId, LocalTime pickupTime);
    
    @Query("SELECT b FROM BusSlot b WHERE b.status = 'ACTIVE' AND (b.maxCapacity - b.currentBookings) > 0")
    List<BusSlot> findSlotsWithAvailableSeats();
    
    @Query("SELECT COUNT(b), SUM(b.maxCapacity), SUM(b.currentBookings) FROM BusSlot b WHERE b.route.id = :routeId")
    Object[] getStatisticsByRoute(@Param("routeId") Long routeId);
    
    long countByStatus(BUS_SLOT_STATUS status);
    
    @Query("SELECT SUM(b.maxCapacity) FROM BusSlot b")
    Long getTotalCapacity();
    
    @Query("SELECT SUM(b.currentBookings) FROM BusSlot b")
    Long getTotalBookings();
    
    @Query("SELECT AVG(b.fareAmount) FROM BusSlot b")
    Double getAverageFare();
}