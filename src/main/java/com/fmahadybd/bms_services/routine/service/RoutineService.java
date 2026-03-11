package com.fmahadybd.bms_services.routine.service;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.routine.dto.CreateRoutineRequest;
import com.fmahadybd.bms_services.routine.dto.RoutineResponse;
import com.fmahadybd.bms_services.routine.model.ClassRoutine;
import com.fmahadybd.bms_services.routine.repository.RoutineRepository;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RouteRepository routeRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest req) {
        // Check for conflicts
        List<ClassRoutine> conflicts = routineRepository.findConflictingRoutines(
                req.getRoomNumber(), req.getDay(), req.getStartTime(), req.getEndTime());
        
        if (!conflicts.isEmpty()) {
            throw new DuplicateResourceException("Room is already booked at this time");
        }

        ClassRoutine routine = ClassRoutine.builder()
                .courseName(req.getCourseName())
                .courseCode(req.getCourseCode())
                .teacherName(req.getTeacherName())
                .day(req.getDay())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .roomNumber(req.getRoomNumber())
                .department(req.getDepartment())
                .batch(req.getBatch())
                .routineType(req.getRoutineType())
                .isActive(true)
                .build();

        if (req.getRouteId() != null) {
            Route route = routeRepository.findById(req.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
            routine.setRoute(route);
        }

        return toResponse(routineRepository.save(routine));
    }

    @Transactional
    public RoutineResponse assignStudentsToRoutine(Long routineId, List<Long> studentIds) {
        ClassRoutine routine = getRoutineOrThrow(routineId);
        
        List<Student> students = studentRepository.findAllById(studentIds);
        routine.getStudents().addAll(students);
        
        return toResponse(routineRepository.save(routine));
    }

    public List<RoutineResponse> getRoutineByDepartmentAndBatch(String department, String batch) {
        return routineRepository.findByDepartmentAndBatch(department, batch).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RoutineResponse> getRoutineByDay(DAY day) {
        return routineRepository.findByDay(day).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RoutineResponse> getRoutineForStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        return routineRepository.findByStudentId(student.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRoutine(Long id) {
        if (!routineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Routine not found");
        }
        routineRepository.deleteById(id);
    }

    private ClassRoutine getRoutineOrThrow(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found"));
    }

    private RoutineResponse toResponse(ClassRoutine r) {
        return RoutineResponse.builder()
                .id(r.getId())
                .courseName(r.getCourseName())
                .courseCode(r.getCourseCode())
                .teacherName(r.getTeacherName())
                .day(r.getDay().toString())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .roomNumber(r.getRoomNumber())
                .department(r.getDepartment())
                .batch(r.getBatch())
                .routineType(r.getRoutineType().toString())
                .isActive(r.isActive())
                .routeId(r.getRoute() != null ? r.getRoute().getId() : null)
                .busNo(r.getRoute() != null ? r.getRoute().getBusNo() : null)
                .studentCount(r.getStudents().size())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}