package com.fmahadybd.bms_services.student.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.route.repository.RouteRepository;
import com.fmahadybd.bms_services.routine.model.ClassRoutine;
import com.fmahadybd.bms_services.routine.repository.RoutineRepository;
import com.fmahadybd.bms_services.student.dto.RegisterStudentRequest;
import com.fmahadybd.bms_services.student.dto.StudentResponse;
import com.fmahadybd.bms_services.student.dto.StudentRoutineResponse;
import com.fmahadybd.bms_services.student.dto.UpdateStudentRequest;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final RouteRepository routeRepository;
    private final RoutineRepository routineRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Register Student ────────────────────────────────────────────────
    @Transactional
    public StudentResponse register(RegisterStudentRequest req) {
        // Check for duplicates
        if (studentRepository.existsByStudentId(req.getStudentId()))
            throw new DuplicateResourceException("Student ID already exists: " + req.getStudentId());
        if (studentRepository.existsByEmail(req.getEmail()))
            throw new DuplicateResourceException("Email already registered: " + req.getEmail());
        if (studentRepository.existsByPhoneNumber(req.getPhoneNumber()))
            throw new DuplicateResourceException("Phone number already registered: " + req.getPhoneNumber());

        Student student = Student.builder()
                .studentId(req.getStudentId())
                .name(req.getName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .address(req.getAddress())
                .department(req.getDepartment())
                .batch(req.getBatch())
                .gender(req.getGender())
                .shift(req.getShift())
                .isBlocked(false)
                .password(passwordEncoder.encode(req.getPassword()))
                .build();

        return toResponse(studentRepository.save(student));
    }

    // ── Find by Student ID ───────────────────────────────────────────────
    public StudentResponse findByStudentId(String studentId) {
        return toResponse(getStudentOrThrow(studentId));
    }

    // ── Find by Email ────────────────────────────────────────────────────
    public StudentResponse findByEmail(String email) {
        return toResponse(studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email)));
    }

    // ── Find by Phone ────────────────────────────────────────────────────
    public StudentResponse findByPhoneNumber(String phone) {
        return toResponse(studentRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with phone: " + phone)));
    }

    // ── Find by Department and Batch ─────────────────────────────────────
    public List<StudentResponse> findByDepartmentAndBatch(String department, String batch) {
        // You'll need to add this method to repository
        return studentRepository.findByDepartmentAndBatch(department, batch).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Find by Route ────────────────────────────────────────────────────
    public List<StudentResponse> findByRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + routeId));
        
        // You'll need to add this method to repository
        return studentRepository.findByRoute(route).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get All Students ─────────────────────────────────────────────────
    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update Student ───────────────────────────────────────────────────
    @Transactional
    public StudentResponse update(String studentId, UpdateStudentRequest req) {
        Student student = getStudentOrThrow(studentId);

        // Check email uniqueness if being updated
        if (req.getEmail() != null && !req.getEmail().equals(student.getEmail())) {
            if (studentRepository.existsByEmail(req.getEmail()))
                throw new DuplicateResourceException("Email already registered: " + req.getEmail());
            student.setEmail(req.getEmail());
        }

        // Check phone uniqueness if being updated
        if (req.getPhoneNumber() != null && !req.getPhoneNumber().equals(student.getPhoneNumber())) {
            if (studentRepository.existsByPhoneNumber(req.getPhoneNumber()))
                throw new DuplicateResourceException("Phone number already registered: " + req.getPhoneNumber());
            student.setPhoneNumber(req.getPhoneNumber());
        }

        if (req.getName() != null) student.setName(req.getName());
        if (req.getAddress() != null) student.setAddress(req.getAddress());
        if (req.getShift() != null) student.setShift(req.getShift());

        return toResponse(studentRepository.save(student));
    }

    // ── Assign Route to Student ──────────────────────────────────────────
    @Transactional
    public StudentResponse assignRoute(String studentId, Long routeId) {
        Student student = getStudentOrThrow(studentId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + routeId));

        student.setRoute(route);
        return toResponse(studentRepository.save(student));
    }

    // ── Remove Route from Student ────────────────────────────────────────
    @Transactional
    public StudentResponse removeRoute(String studentId) {
        Student student = getStudentOrThrow(studentId);
        student.setRoute(null);
        return toResponse(studentRepository.save(student));
    }

    // ── Get Student's Routines ───────────────────────────────────────────
    public List<StudentRoutineResponse> getStudentRoutines(String studentId) {
        Student student = getStudentOrThrow(studentId);
        
        return student.getRoutines().stream()
                .map(this::toRoutineResponse)
                .collect(Collectors.toList());
    }

    // ── Block/Unblock Student ────────────────────────────────────────────
    @Transactional
    public StudentResponse setBlockStatus(String studentId, boolean blocked) {
        Student student = getStudentOrThrow(studentId);
        student.setBlocked(blocked);
        return toResponse(studentRepository.save(student));
    }

    // ── Delete Student ───────────────────────────────────────────────────
    @Transactional
    public void deleteStudent(String studentId) {
        Student student = getStudentOrThrow(studentId);
        studentRepository.delete(student);
    }

    // ── Change Password ──────────────────────────────────────────────────
    @Transactional
    public void changePassword(String studentId, String oldPassword, String newPassword) {
        Student student = getStudentOrThrow(studentId);
        
        if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        
        student.setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
    }

    // ── Helper Methods ───────────────────────────────────────────────────
    private Student getStudentOrThrow(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .studentId(s.getStudentId())
                .name(s.getName())
                .email(s.getEmail())
                .phoneNumber(s.getPhoneNumber())
                .address(s.getAddress())
                .department(s.getDepartment())
                .batch(s.getBatch())
                .gender(s.getGender())
                .isBlocked(s.isBlocked())
                .shift(s.getShift())
                .routeId(s.getRoute() != null ? s.getRoute().getId() : null)
                .routeName(s.getRoute() != null ? s.getRoute().getRouteName() : null)
                .busNo(s.getRoute() != null ? s.getRoute().getBusNo() : null)
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    private StudentRoutineResponse toRoutineResponse(ClassRoutine r) {
        return StudentRoutineResponse.builder()
                .routineId(r.getId())
                .courseName(r.getCourseName())
                .courseCode(r.getCourseCode())
                .teacherName(r.getTeacherName())
                .day(r.getDay().toString())
                .startTime(r.getStartTime())
                .endTime(r.getEndTime())
                .roomNumber(r.getRoomNumber())
                .routineType(r.getRoutineType().toString())
                .build();
    }
}