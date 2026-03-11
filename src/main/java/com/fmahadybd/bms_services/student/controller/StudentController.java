package com.fmahadybd.bms_services.student.controller;

import com.fmahadybd.bms_services.student.dto.*;
import com.fmahadybd.bms_services.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // ── Register Student ─────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<StudentResponse> register(
            @Valid @RequestBody RegisterStudentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.register(req));
    }

    // ── Student Login ────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody StudentLoginRequest req) {
        // This should be handled by Spring Security with JWT
        // Return appropriate response
        return ResponseEntity.ok("Login endpoint - Implement JWT authentication");
    }

    // ── Get All Students ─────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    // ── Get Student by ID ────────────────────────────────────────────────
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getByStudentId(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.findByStudentId(studentId));
    }

    // ── Get Student by Email ─────────────────────────────────────────────
    @GetMapping("/search/email")
    public ResponseEntity<StudentResponse> getByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(studentService.findByEmail(email));
    }

    // ── Get Student by Phone ─────────────────────────────────────────────
    @GetMapping("/search/phone")
    public ResponseEntity<StudentResponse> getByPhone(
            @RequestParam String phone) {
        return ResponseEntity.ok(studentService.findByPhoneNumber(phone));
    }

    // ── Get Students by Department and Batch ─────────────────────────────
    @GetMapping("/department/{department}/batch/{batch}")
    public ResponseEntity<List<StudentResponse>> getByDepartmentAndBatch(
            @PathVariable String department,
            @PathVariable String batch) {
        return ResponseEntity.ok(studentService.findByDepartmentAndBatch(department, batch));
    }

    // ── Get Students by Route ────────────────────────────────────────────
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<StudentResponse>> getByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.findByRoute(routeId));
    }

    // ── Update Student ───────────────────────────────────────────────────
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable String studentId,
            @Valid @RequestBody UpdateStudentRequest req) {
        return ResponseEntity.ok(studentService.update(studentId, req));
    }

    // ── Assign Route to Student ──────────────────────────────────────────
    @PatchMapping("/{studentId}/assign-route/{routeId}")
    public ResponseEntity<StudentResponse> assignRoute(
            @PathVariable String studentId,
            @PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.assignRoute(studentId, routeId));
    }

    // ── Remove Route from Student ────────────────────────────────────────
    @PatchMapping("/{studentId}/remove-route")
    public ResponseEntity<StudentResponse> removeRoute(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.removeRoute(studentId));
    }

    // ── Get Student's Routines ───────────────────────────────────────────
    @GetMapping("/{studentId}/routines")
    public ResponseEntity<List<StudentRoutineResponse>> getStudentRoutines(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentRoutines(studentId));
    }

    // ── Block Student ────────────────────────────────────────────────────
    @PatchMapping("/{studentId}/block")
    public ResponseEntity<StudentResponse> blockStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, true));
    }

    // ── Unblock Student ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/unblock")
    public ResponseEntity<StudentResponse> unblockStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, false));
    }

    // ── Change Password ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String studentId,
            @RequestBody ChangePasswordRequest req) {
        studentService.changePassword(studentId, req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }

    // ── Delete Student ───────────────────────────────────────────────────
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}