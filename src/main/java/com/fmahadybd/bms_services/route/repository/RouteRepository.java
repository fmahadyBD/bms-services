package com.fmahadybd.bms_services.route.repository;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    Optional<Route> findByBusNo(String busNo);
    
    boolean existsByBusNo(String busNo);
    
    List<Route> findByStatus(ROUTE_STATUS status);
    
    @Query("SELECT DISTINCT r FROM Route r JOIN r.operatingDays d WHERE d.day = :day")
    List<Route> findByOperatingDays_Day(@Param("day") DAY day);
    
    @Query("SELECT r FROM Route r WHERE r.status = :status AND EXISTS (SELECT d FROM r.operatingDays d WHERE d.day = :day)")
    List<Route> findByStatusAndDay(@Param("status") ROUTE_STATUS status, @Param("day") DAY day);
    
    @Query("SELECT COUNT(b) FROM Route r JOIN r.buses b WHERE r.id = :routeId")
    long countBusesByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT COUNT(bs) FROM Route r JOIN r.busSlots bs WHERE r.id = :routeId")
    long countSlotsByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT COUNT(b) FROM Route r JOIN r.buses b WHERE r.id = :routeId AND b.status = 'ACTIVE'")
    long countActiveBusesByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT COUNT(bs) FROM Route r JOIN r.busSlots bs WHERE r.id = :routeId AND bs.status = 'ACTIVE'")
    long countActiveSlotsByRouteId(@Param("routeId") Long routeId);
}