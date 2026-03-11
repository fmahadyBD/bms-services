package com.fmahadybd.bms_services.student.repository;

import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByPhoneNumber(String phoneNumber);
    Optional<Student> findByEmailOrPhoneNumber(String email, String phoneNumber);
    Optional<Student> findByStudentIdOrEmailOrPhoneNumber(String studentId, String email, String phoneNumber);

    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // Add these new methods
    List<Student> findByDepartmentAndBatch(String department, String batch);
    List<Student> findByRoute(Route route);
    List<Student> findByDepartment(String department);
    List<Student> findByBatch(String batch);
    List<Student> findByShift(String shift);
    List<Student> findByIsBlocked(boolean isBlocked);
}