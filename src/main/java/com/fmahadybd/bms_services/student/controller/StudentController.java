package com.fmahadybd.bms_services.student.controller;

import com.fmahadybd.bms_services.student.dto.*;
import com.fmahadybd.bms_services.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;


    // ── Register Student (Manager only) ─────────────────────────────────
    // @PostMapping("/register")
    // @PreAuthorize("hasRole('MANAGER')")
    // public ResponseEntity<StudentResponse> register(
    //         @Valid @RequestBody RegisterStudentRequest req) {
    //     return ResponseEntity.status(HttpStatus.CREATED)
    //             .body(studentService.register(req));
    // }

    // ── Get All Students ─────────────────────────────────────────────────
    @GetMapping
    // @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    // ── Get Student by ID ────────────────────────────────────────────────
    @GetMapping("/{studentId}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('STUDENT') and @studentSecurity.isSelf(authentication, #studentId))")
    public ResponseEntity<StudentResponse> getByStudentId(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.findByStudentId(studentId));
    }

    // ── Get Student by Email ─────────────────────────────────────────────
    @GetMapping("/search/email")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> getByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(studentService.findByEmail(email));
    }

    // ── Get Student by Phone ─────────────────────────────────────────────
    @GetMapping("/search/phone")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> getByPhone(
            @RequestParam String phone) {
        return ResponseEntity.ok(studentService.findByPhoneNumber(phone));
    }

    // ── Get Students by Department and Batch ─────────────────────────────
    @GetMapping("/department/{department}/batch/{batch}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<StudentResponse>> getByDepartmentAndBatch(
            @PathVariable String department,
            @PathVariable String batch) {
        return ResponseEntity.ok(studentService.findByDepartmentAndBatch(department, batch));
    }

    // ── Get Students by Route ────────────────────────────────────────────
    @GetMapping("/route/{routeId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<StudentResponse>> getByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.findByRoute(routeId));
    }

    // ── Update Student ───────────────────────────────────────────────────
    @PatchMapping("/{studentId}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('STUDENT') and @studentSecurity.isSelf(authentication, #studentId))")
    public ResponseEntity<StudentResponse> update(
            @PathVariable String studentId,
            @Valid @RequestBody UpdateStudentRequest req) {
        return ResponseEntity.ok(studentService.update(studentId, req));
    }

    // ── Assign Route to Student ──────────────────────────────────────────
    @PatchMapping("/{studentId}/assign-route/{routeId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> assignRoute(
            @PathVariable String studentId,
            @PathVariable Long routeId) {
        return ResponseEntity.ok(studentService.assignRoute(studentId, routeId));
    }

    // ── Remove Route from Student ────────────────────────────────────────
    @PatchMapping("/{studentId}/remove-route")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> removeRoute(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.removeRoute(studentId));
    }

    // ── Get Student's Routines ───────────────────────────────────────────
    @GetMapping("/{studentId}/routines")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('STUDENT') and @studentSecurity.isSelf(authentication, #studentId))")
    public ResponseEntity<List<StudentRoutineResponse>> getStudentRoutines(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentRoutines(studentId));
    }

    // ── Block Student ────────────────────────────────────────────────────
    @PatchMapping("/{studentId}/block")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> blockStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, true));
    }

    // ── Unblock Student ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/unblock")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StudentResponse> unblockStudent(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, false));
    }

    // ── Change Password ──────────────────────────────────────────────────
    @PatchMapping("/{studentId}/change-password")
    @PreAuthorize("hasRole('STUDENT') and @studentSecurity.isSelf(authentication, #studentId)")
    public ResponseEntity<Void> changePassword(
            @PathVariable String studentId,
            @Valid @RequestBody ChangePasswordRequest req) {
        studentService.changePassword(studentId, req.getOldPassword(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }

    // ── Delete Student ───────────────────────────────────────────────────
    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}