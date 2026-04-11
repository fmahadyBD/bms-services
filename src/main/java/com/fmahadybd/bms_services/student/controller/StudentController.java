package com.fmahadybd.bms_services.student.controller;

import com.fmahadybd.bms_services.student.dto.*;
import com.fmahadybd.bms_services.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    // ✅ FAST: Paginated summary endpoint
    @GetMapping
    public ResponseEntity<Page<StudentSummaryResponse>> getAllStudentsSummary(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching students with pagination - page: {}, size: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<StudentSummaryResponse> result = studentService.findAllSummary(pageable);
            log.info("Successfully fetched {} students out of {}", 
                     result.getNumberOfElements(), result.getTotalElements());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error fetching students: {}", e.getMessage(), e);
            throw e;
        }
    }

    // ── Get Student by ID ────────────────────────────────────────────────
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getByStudentId(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.findByStudentId(studentId));
    }

    // ── Get Student by Email ─────────────────────────────────────────────
    @GetMapping("/search/email")
    public ResponseEntity<StudentResponse> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(studentService.findByEmail(email));
    }

    // ── Get Student by Phone ─────────────────────────────────────────────
    @GetMapping("/search/phone")
    public ResponseEntity<StudentResponse> getByPhone(@RequestParam String phone) {
        return ResponseEntity.ok(studentService.findByPhoneNumber(phone));
    }

    // ── Get Students by Department and Batch ─────────────────────────────
    @GetMapping("/department/{department}/batch/{batch}")
    public ResponseEntity<List<StudentResponse>> getByDepartmentAndBatch(
            @PathVariable String department, @PathVariable String batch) {
        return ResponseEntity.ok(studentService.findByDepartmentAndBatch(department, batch));
    }

    // ── Get Students by Route ────────────────────────────────────────────
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<StudentResponse>> getByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.findByRoute(routeId));
    }

    // ── Update Student ───────────────────────────────────────────────────
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable String studentId, @Valid @RequestBody UpdateStudentRequest req) {
        return ResponseEntity.ok(studentService.update(studentId, req));
    }

    // ── Assign Route to Student ──────────────────────────────────────────
    @PatchMapping("/{studentId}/assign-route/{routeId}")
    public ResponseEntity<StudentResponse> assignRoute(
            @PathVariable String studentId, @PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.assignRoute(studentId, routeId));
    }

    // ── Remove Route from Student ────────────────────────────────────────
    @PatchMapping("/{studentId}/remove-route")
    public ResponseEntity<StudentResponse> removeRoute(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.removeRoute(studentId));
    }

    // ── Get Student's Routines ───────────────────────────────────────────
    @GetMapping("/{studentId}/routines")
    public ResponseEntity<List<StudentRoutineResponse>> getStudentRoutines(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentRoutines(studentId));
    }

    // ── Block Student ────────────────────────────────────────────────────
    @PatchMapping("/{studentId}/block")
    public ResponseEntity<StudentResponse> blockStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, true));
    }

    // ── Unblock Student ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/unblock")
    public ResponseEntity<StudentResponse> unblockStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, false));
    }

    // ── Change Password ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable String studentId, @Valid @RequestBody ChangePasswordRequest req) {
        try {
            studentService.changePassword(studentId, req.getOldPassword(), req.getNewPassword());
            return ResponseEntity.ok().body("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── Delete Student ───────────────────────────────────────────────────
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}