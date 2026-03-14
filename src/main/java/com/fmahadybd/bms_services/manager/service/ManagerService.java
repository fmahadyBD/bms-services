package com.fmahadybd.bms_services.manager.service;

import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.exception.UnauthorizedException;
import com.fmahadybd.bms_services.manager.dto.*;
import com.fmahadybd.bms_services.manager.model.Manager;
import com.fmahadybd.bms_services.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Create Manager ────────────────────────────────────────────────────
    @Transactional
    public ManagerResponse createManager(ManagerRequest request) {
        // Check for duplicates
        if (managerRepository.existsByManagerId(request.getManagerId())) {
            throw new DuplicateResourceException("Manager ID already exists: " + request.getManagerId());
        }
        if (managerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        if (managerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already exists: " + request.getPhoneNumber());
        }

        Manager manager = Manager.builder()
                .managerId(request.getManagerId())
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .password(passwordEncoder.encode(request.getPassword()))
                .isBlocked(false)
                .build();

        Manager savedManager = managerRepository.save(manager);
        return convertToResponse(savedManager);
    }

    // ── Get All Managers ──────────────────────────────────────────────────
    public List<ManagerResponse> getAllManagers() {
        return managerRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Get Manager by ID ─────────────────────────────────────────────────
    public ManagerResponse getManagerById(Long id) {
        return convertToResponse(getManagerOrThrow(id));
    }

    // ── Get Manager by Email ──────────────────────────────────────────────
    public ManagerResponse getManagerByEmail(String email) {
        Manager manager = managerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with email: " + email));
        return convertToResponse(manager);
    }

    // ── Get Manager by Manager ID ─────────────────────────────────────────
    public ManagerResponse getManagerByManagerId(String managerId) {
        Manager manager = managerRepository.findByManagerId(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with manager ID: " + managerId));
        return convertToResponse(manager);
    }

    // ── Get Manager by Phone ──────────────────────────────────────────────
    public ManagerResponse getManagerByPhone(String phoneNumber) {
        Manager manager = managerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with phone: " + phoneNumber));
        return convertToResponse(manager);
    }

    // ── Update Manager ────────────────────────────────────────────────────
    @Transactional
    public ManagerResponse updateManager(Long id, ManagerUpdateRequest request) {
        Manager manager = getManagerOrThrow(id);

        // Check email uniqueness if being updated
        if (request.getEmail() != null && !request.getEmail().equals(manager.getEmail())) {
            if (managerRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already exists: " + request.getEmail());
            }
            manager.setEmail(request.getEmail());
        }

        // Check phone uniqueness if being updated
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(manager.getPhoneNumber())) {
            if (managerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new DuplicateResourceException("Phone number already exists: " + request.getPhoneNumber());
            }
            manager.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getName() != null) manager.setName(request.getName());
        if (request.getAddress() != null) manager.setAddress(request.getAddress());
        if (request.getDepartment() != null) manager.setDepartment(request.getDepartment());
        if (request.getDesignation() != null) manager.setDesignation(request.getDesignation());

        return convertToResponse(managerRepository.save(manager));
    }

    // ── Partial Update ────────────────────────────────────────────────────
    @Transactional
    public ManagerResponse partialUpdateManager(Long id, Map<String, Object> updates) {
        Manager manager = getManagerOrThrow(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    manager.setName((String) value);
                    break;
                case "email":
                    String newEmail = (String) value;
                    if (!newEmail.equals(manager.getEmail()) && managerRepository.existsByEmail(newEmail)) {
                        throw new DuplicateResourceException("Email already exists: " + newEmail);
                    }
                    manager.setEmail(newEmail);
                    break;
                case "phoneNumber":
                    String newPhone = (String) value;
                    if (!newPhone.equals(manager.getPhoneNumber()) && managerRepository.existsByPhoneNumber(newPhone)) {
                        throw new DuplicateResourceException("Phone number already exists: " + newPhone);
                    }
                    manager.setPhoneNumber(newPhone);
                    break;
                case "address":
                    manager.setAddress((String) value);
                    break;
                case "department":
                    manager.setDepartment((String) value);
                    break;
                case "designation":
                    manager.setDesignation((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field for update: " + key);
            }
        });

        return convertToResponse(managerRepository.save(manager));
    }

    // ── Update Manager Status (Block/Unblock) ────────────────────────────
    @Transactional
    public ManagerResponse updateManagerStatus(Long id, ManagerStatusUpdateRequest request) {
        Manager manager = getManagerOrThrow(id);
        manager.setBlocked(request.getBlocked());
        return convertToResponse(managerRepository.save(manager));
    }

    // ── Change Password ───────────────────────────────────────────────────
    @Transactional
    public void changePassword(Long id, ManagerPasswordChangeRequest request, Long currentManagerId) {
        // Only allow managers to change their own password or admins to change any
        if (!id.equals(currentManagerId)) {
            throw new UnauthorizedException("You can only change your own password");
        }

        Manager manager = getManagerOrThrow(id);

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), manager.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Verify new password matches confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Update password
        manager.setPassword(passwordEncoder.encode(request.getNewPassword()));
        managerRepository.save(manager);
    }

    // ── Delete Manager ────────────────────────────────────────────────────
    @Transactional
    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Manager not found with id: " + id);
        }
        managerRepository.deleteById(id);
    }

    // ── Filter Managers ───────────────────────────────────────────────────
    public List<ManagerResponse> filterManagers(ManagerFilterRequest filter) {
        List<Manager> managers = managerRepository.findAll();

        if (filter.getName() != null && !filter.getName().isEmpty()) {
            managers = managers.stream()
                    .filter(m -> m.getName().toLowerCase().contains(filter.getName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            managers = managers.stream()
                    .filter(m -> m.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isEmpty()) {
            managers = managers.stream()
                    .filter(m -> m.getPhoneNumber().contains(filter.getPhoneNumber()))
                    .collect(Collectors.toList());
        }

        if (filter.getDepartment() != null && !filter.getDepartment().isEmpty()) {
            managers = managers.stream()
                    .filter(m -> m.getDepartment().equalsIgnoreCase(filter.getDepartment()))
                    .collect(Collectors.toList());
        }

        if (filter.getDesignation() != null && !filter.getDesignation().isEmpty()) {
            managers = managers.stream()
                    .filter(m -> m.getDesignation().equalsIgnoreCase(filter.getDesignation()))
                    .collect(Collectors.toList());
        }

        if (filter.getIsBlocked() != null) {
            managers = managers.stream()
                    .filter(m -> m.isBlocked() == filter.getIsBlocked())
                    .collect(Collectors.toList());
        }

        return managers.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // ── Get Manager Statistics ────────────────────────────────────────────
    public ManagerStatistics getManagerStatistics() {
        long totalManagers = managerRepository.count();
        long activeManagers = managerRepository.findAll().stream()
                .filter(m -> !m.isBlocked())
                .count();
        long blockedManagers = managerRepository.findAll().stream()
                .filter(Manager::isBlocked)
                .count();

        return ManagerStatistics.builder()
                .totalManagers(totalManagers)
                .activeManagers(activeManagers)
                .blockedManagers(blockedManagers)
                .build();
    }

    // ── Get Managers by Department ────────────────────────────────────────
    public List<ManagerResponse> getManagersByDepartment(String department) {
        return managerRepository.findAll().stream()
                .filter(m -> m.getDepartment().equalsIgnoreCase(department))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ── Helper Methods ────────────────────────────────────────────────────
    private Manager getManagerOrThrow(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + id));
    }

    private ManagerResponse convertToResponse(Manager manager) {
        return ManagerResponse.builder()
                .id(manager.getId())
                .managerId(manager.getManagerId())
                .name(manager.getName())
                .email(manager.getEmail())
                .phoneNumber(manager.getPhoneNumber())
                .address(manager.getAddress())
                .department(manager.getDepartment())
                .designation(manager.getDesignation())
                .isBlocked(manager.isBlocked())
                .createdAt(manager.getCreatedAt())
                .updatedAt(manager.getUpdatedAt())
                .build();
    }
}