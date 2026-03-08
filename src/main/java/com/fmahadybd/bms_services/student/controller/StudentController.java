package com.fmahadybd.bms_services.student.controller;

import com.fmahadybd.bms_services.student.dto.*;
import com.fmahadybd.bms_services.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> register(
            @Valid @RequestBody RegisterStudentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.register(req));
    }

 
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getByStudentId(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.findByStudentId(studentId));
    }

  
    @GetMapping("/search/email")
    public ResponseEntity<StudentResponse> getByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(studentService.findByEmail(email));
    }

    
    @GetMapping("/search/phone")
    public ResponseEntity<StudentResponse> getByPhone(
            @RequestParam String phone) {
        return ResponseEntity.ok(studentService.findByPhoneNumber(phone));
    }

    
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable String studentId,
            @Valid @RequestBody UpdateStudentRequest req) {
        return ResponseEntity.ok(studentService.update(studentId, req));
    }

  
    @PatchMapping("/{studentId}/block")
    public ResponseEntity<StudentResponse> block(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, true));
    }

    
    @PatchMapping("/{studentId}/unblock")
    public ResponseEntity<StudentResponse> unblock(
            @PathVariable String studentId) {
        return ResponseEntity.ok(studentService.setBlockStatus(studentId, false));
    }
}