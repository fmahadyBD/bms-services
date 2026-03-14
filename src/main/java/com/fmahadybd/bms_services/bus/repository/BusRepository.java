package com.fmahadybd.bms_services.bus.repository;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.bus.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    Optional<Bus> findByBusNumber(String busNumber);

    List<Bus> findByStatus(BUS_STATUS status);

    List<Bus> findByRouteId(Long routeId);

    List<Bus> findByRouteIdAndStatus(Long routeId, BUS_STATUS status);

    List<Bus> findByDriverNameContainingIgnoreCase(String driverName);

    List<Bus> findByBusNameContainingIgnoreCase(String busName);

    boolean existsByBusNumber(String busNumber);

    @Query("SELECT b FROM Bus b WHERE b.route.id = :routeId AND b.status = 'ACTIVE'")
    List<Bus> findActiveBusesByRoute(@Param("routeId") Long routeId);

    long countByStatus(BUS_STATUS status);

    long countByRouteId(Long routeId);

    @Query("SELECT COUNT(b) FROM Bus b WHERE b.route.id = :routeId AND b.status = :status")
    long countByRouteIdAndStatus(@Param("routeId") Long routeId, @Param("status") BUS_STATUS status);
}