package com.fmahadybd.bms_services.student.service;

import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.student.dto.*;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponse register(RegisterStudentRequest req) {
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


    public StudentResponse findByStudentId(String studentId) {
        return toResponse(getOrThrow(studentId));
    }


    public StudentResponse findByEmail(String email) {
        return toResponse(studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with email: " + email)));
    }

    public StudentResponse findByPhoneNumber(String phone) {
        return toResponse(studentRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with phone: " + phone)));
    }


    @Transactional
    public StudentResponse update(String studentId, UpdateStudentRequest req) {
        Student student = getOrThrow(studentId);

        if (req.getName() != null)        student.setName(req.getName());
        if (req.getEmail() != null)       student.setEmail(req.getEmail());
        if (req.getPhoneNumber() != null) student.setPhoneNumber(req.getPhoneNumber());
        if (req.getAddress() != null)     student.setAddress(req.getAddress());
        if (req.getShift() != null)       student.setShift(req.getShift());

        return toResponse(studentRepository.save(student));
    }


    @Transactional
    public StudentResponse setBlockStatus(String studentId, boolean blocked) {
        Student student = getOrThrow(studentId);
        student.setBlocked(blocked);
        return toResponse(studentRepository.save(student));
    }

    private Student getOrThrow(String studentId) {
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
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}