package com.fmahadybd.bms_services.auth.service;

import com.fmahadybd.bms_services.auth.BaseUser;
import com.fmahadybd.bms_services.auth.dto.AuthenticationRequest;
import com.fmahadybd.bms_services.auth.dto.AuthenticationResponse;
import com.fmahadybd.bms_services.auth.dto.ManagerRegistrationRequest;
import com.fmahadybd.bms_services.auth.dto.RegisterStudentRequest;
import com.fmahadybd.bms_services.auth.model.Token;
import com.fmahadybd.bms_services.auth.model.TokenType;
import com.fmahadybd.bms_services.auth.repository.TokenRepository;
import com.fmahadybd.bms_services.auth.security.JwtService;
import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.manager.model.Manager;
import com.fmahadybd.bms_services.manager.repository.ManagerRepository;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import com.fmahadybd.bms_services.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final ManagerRepository managerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StudentService studentService;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public void registerStudent(RegisterStudentRequest req) {
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

        studentRepository.save(student);
    }

    @Transactional
    public void registerManager(ManagerRegistrationRequest request) {
        // FIX: consistent use of DuplicateResourceException instead of IllegalStateException
        if (studentRepository.findByEmail(request.getEmail()).isPresent() ||
            managerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        var manager = Manager.builder()
                .name(request.getFirstname() + " " + request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .managerId(request.getEmployeeId())
                .department(request.getDepartment())
                .designation(request.getPosition())
                .isBlocked(false)
                .build();

        managerRepository.save(manager);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var claims = new HashMap<String, Object>();
            var user = (BaseUser) auth.getPrincipal();

            claims.put("username", user.getUsername());
            claims.put("fullName", user.getName());
            claims.put("userType", user.getUserType());
            // FIX: removed random + timestamp claims — DB token store handles invalidation

            if (user instanceof Student student) {
                claims.put("studentId", student.getStudentId());
                claims.put("department", student.getDepartment());
                claims.put("role", "STUDENT");
            } else if (user instanceof Manager manager) {
                claims.put("managerId", manager.getManagerId());
                claims.put("department", manager.getDepartment());
                claims.put("designation", manager.getDesignation());
                claims.put("role", "MANAGER");
            }

            var jwtToken = jwtService.generateToken(claims, user);

            // FIX: save new token FIRST, then revoke old ones — avoids the
            // window where the user has zero valid tokens between the two calls
            saveUserToken(user, jwtToken);
            revokeOldUserTokens(user, jwtToken);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .userType(user.getUserType())
                    .role(user instanceof Student ? "STUDENT" : "MANAGER")
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private void saveUserToken(BaseUser user, String jwtToken) {
        // FIX: always insert a fresh token — no reuse logic needed since we
        // revoke old tokens right after. Keeps this method simple and correct.
        var token = Token.builder()
                .userId(user.getId())
                .userType(user.getUserType())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .expiresAt(LocalDateTime.now().plus(jwtExpiration, ChronoUnit.MILLIS))
                .build();
        tokenRepository.save(token);
    }

    // FIX: renamed to revokeOldUserTokens and takes the new token string
    // so it can exclude it from the revocation query
    private void revokeOldUserTokens(BaseUser user, String newToken) {
        var oldTokens = tokenRepository.findAllValidTokensByUserExcluding(
                user.getId(), user.getUserType(), newToken
        );
        if (oldTokens.isEmpty()) return;

        oldTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(oldTokens);
    }
}