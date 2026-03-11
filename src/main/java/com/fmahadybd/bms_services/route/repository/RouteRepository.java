package com.fmahadybd.bms_services.route.repository;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByBusNo(String busNo);
    boolean existsByBusNo(String busNo);
    List<Route> findByStatus(ROUTE_STATUS status);

    // Find all routes that operate on a specific day
    List<Route> findByOperatingDays_Day(DAY day);
}