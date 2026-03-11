package com.fmahadybd.bms_services.routine.repository;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.routine.model.ClassRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<ClassRoutine, Long> {
    
    List<ClassRoutine> findByDepartmentAndBatch(String department, String batch);
    
    List<ClassRoutine> findByDay(DAY day);
    
    List<ClassRoutine> findByTeacherName(String teacherName);
    
    @Query("SELECT r FROM ClassRoutine r WHERE r.roomNumber = :room AND r.day = :day " +
           "AND ((r.startTime BETWEEN :start AND :end) OR (r.endTime BETWEEN :start AND :end))")
    List<ClassRoutine> findConflictingRoutines(
            @Param("room") String room,
            @Param("day") DAY day,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end);
    
    @Query("SELECT r FROM ClassRoutine r JOIN r.students s WHERE s.id = :studentId")
    List<ClassRoutine> findByStudentId(@Param("studentId") Long studentId);
    
    boolean existsByCourseCodeAndBatch(String courseCode, String batch);
}