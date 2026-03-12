package com.fmahadybd.bms_services.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fmahadybd.bms_services.auth.dto.AuthenticationRequest;
import com.fmahadybd.bms_services.auth.dto.AuthenticationResponse;
import com.fmahadybd.bms_services.auth.dto.ManagerRegistrationRequest;
import com.fmahadybd.bms_services.auth.service.AuthenticationService;
import com.fmahadybd.bms_services.student.dto.RegisterStudentRequest;

@RestController 
@RequestMapping("auth") 
@RequiredArgsConstructor 
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register/student")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerStudent(
            @RequestBody @Valid RegisterStudentRequest request
    ) {
        service.registerStudent(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/manager")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerManager(
            @RequestBody @Valid ManagerRegistrationRequest request
    ) {
        service.registerManager(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}