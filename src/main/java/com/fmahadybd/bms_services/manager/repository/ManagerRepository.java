package com.fmahadybd.bms_services.manager.repository;

import com.fmahadybd.bms_services.manager.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
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
}