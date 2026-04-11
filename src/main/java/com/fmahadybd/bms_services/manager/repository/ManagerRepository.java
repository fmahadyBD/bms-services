package com.fmahadybd.bms_services.manager.repository;

import com.fmahadybd.bms_services.manager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByEmail(String email);

    Optional<Manager> findByManagerId(String managerId);

    Optional<Manager> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByManagerId(String managerId);

    boolean existsByPhoneNumber(String phoneNumber);

    // ManagerRepository.java — add this
    @Query("SELECT COUNT(m), SUM(CASE WHEN m.isBlocked = false THEN 1 ELSE 0 END), SUM(CASE WHEN m.isBlocked = true THEN 1 ELSE 0 END) FROM Manager m")
    Object[] getManagerStats();
}