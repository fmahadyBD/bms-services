package com.fmahadybd.bms_services.slot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BusSlotRepository extends JpaRepository<BusSlot, Long> {

    List<BusSlot> findByRouteId(Long routeId);
    
    List<BusSlot> findByStatus(BUS_SLOT_STATUS status);
    
    List<BusSlot> findByRouteIdAndStatus(Long routeId, BUS_SLOT_STATUS status);
    
    List<BusSlot> findByPickupTimeBetween(LocalTime startTime, LocalTime endTime);
    
    List<BusSlot> findByIsRegularTrue();
    
    List<BusSlot> findByRouteIdAndPickupTimeBetween(Long routeId, LocalTime startTime, LocalTime endTime);
    
    boolean existsByRouteIdAndPickupTime(Long routeId, LocalTime pickupTime);
    
    long countByStatus(BUS_SLOT_STATUS status);
    
    long countByRouteId(Long routeId);
    
    long countByRouteIdAndStatus(Long routeId, BUS_SLOT_STATUS status);
}