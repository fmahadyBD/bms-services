package com.fmahadybd.bms_services.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fmahadybd.bms_services.auth.dto.AuthenticationRequest;
import com.fmahadybd.bms_services.auth.dto.AuthenticationResponse;
import com.fmahadybd.bms_services.auth.dto.ManagerRegistrationRequest;
import com.fmahadybd.bms_services.auth.dto.RegisterStudentRequest;
import com.fmahadybd.bms_services.auth.service.AuthenticationService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    // FIX: removed @ResponseStatus — ResponseEntity already sets the status
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(
            @RequestBody @Valid RegisterStudentRequest request
    ) {
        service.registerStudent(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/manager")
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