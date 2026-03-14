package com.fmahadybd.bms_services.new_bus_request.service;

import com.fmahadybd.bms_services.exception.DuplicateResourceException;
import com.fmahadybd.bms_services.exception.ResourceNotFoundException;
import com.fmahadybd.bms_services.exception.UnauthorizedException;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestRequest;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestResponse;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestStatusUpdateRequest;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestSummaryResponse;
import com.fmahadybd.bms_services.new_bus_request.dto.BusRequestStatistics;
import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import com.fmahadybd.bms_services.new_bus_request.model.BusRequest;
import com.fmahadybd.bms_services.new_bus_request.repository.BusRequestRepository;
import com.fmahadybd.bms_services.route.model.PickupPoint;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.student.dto.StudentResponse;
import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusRequestService {

    private final BusRequestRepository busRequestRepository;
    private final StudentRepository studentRepository;
    // No RouteRepository or PickupPointRepository needed!

    // ── Create Bus Request ─────────────────────────────────────────────────
    @Transactional
    public BusRequestResponse createRequest(BusRequestRequest request, Long requestedByUserId) {
        // Validate student exists
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        // Create proxy references - JPA only needs IDs for relationships
        Route route = Route.builder()
                .id(request.getRouteId())
                .build();

        PickupPoint pickupPoint = PickupPoint.builder()
                .id(request.getPickupPointId())
                .build();

        // Validate dates
        if (request.getRequestedFrom().isAfter(request.getRequestedTo())) {
            throw new IllegalArgumentException("Requested from date must be before requested to date");
        }

        // Check if student already has an active request for overlapping dates
        boolean hasOverlappingRequest = busRequestRepository
                .existsByStudentIdAndRouteIdAndStatusAndRequestedFromLessThanEqualAndRequestedToGreaterThanEqual(
                        request.getStudentId(),
                        request.getRouteId(),
                        BUS_REQUEST_STATUS.APPROVED,
                        request.getRequestedTo(),
                        request.getRequestedFrom()
                );

        if (hasOverlappingRequest) {
            throw new DuplicateResourceException("Student already has an active bus request for overlapping dates");
        }

        // Create new bus request
        BusRequest busRequest = BusRequest.builder()
                .student(student)
                .route(route)
                .pickupPoint(pickupPoint)
                .requestedFrom(request.getRequestedFrom())
                .requestedTo(request.getRequestedTo())
                .reason(request.getReason())
                .status(BUS_REQUEST_STATUS.REQUESTED)
                .build();

        BusRequest savedRequest = busRequestRepository.save(busRequest);
        return toResponse(savedRequest);
    }

    // ── Get All Requests ───────────────────────────────────────────────────
    public List<BusRequestResponse> getAllRequests() {
        return busRequestRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Request by ID ──────────────────────────────────────────────────
    public BusRequestResponse getRequestById(Long id) {
        BusRequest request = getOrThrow(id);
        return toResponse(request);
    }

    // ── Get Requests by Student ────────────────────────────────────────────
    public List<BusRequestSummaryResponse> getRequestsByStudent(Long studentId) {
        return busRequestRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());
    }

    // ── Get Requests by Status ─────────────────────────────────────────────
    public List<BusRequestResponse> getRequestsByStatus(BUS_REQUEST_STATUS status) {
        return busRequestRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Pending Requests ───────────────────────────────────────────────
    public List<BusRequestResponse> getPendingRequests() {
        return busRequestRepository.findByStatusOrderByCreatedAtDesc(BUS_REQUEST_STATUS.REQUESTED).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Active Requests ────────────────────────────────────────────────
    public List<BusRequestResponse> getActiveRequests() {
        return busRequestRepository.findActiveRequests(LocalDate.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update Request Status (Admin only) ─────────────────────────────────
    @Transactional
    public BusRequestResponse updateRequestStatus(Long id, BusRequestStatusUpdateRequest statusUpdate, Long adminId) {
        BusRequest request = getOrThrow(id);

        request.setStatus(statusUpdate.getStatus());
        request.setAdminRemarks(statusUpdate.getAdminRemarks());
        request.setApprovedBy(adminId);

        if (statusUpdate.getStatus() == BUS_REQUEST_STATUS.APPROVED) {
            request.setApprovalDate(LocalDate.now());
        }

        BusRequest updatedRequest = busRequestRepository.save(request);
        return toResponse(updatedRequest);
    }

    // ── Cancel Request (Student) ───────────────────────────────────────────
    @Transactional
    public BusRequestResponse cancelRequest(Long id, Long studentId) {
        BusRequest request = getOrThrow(id);

        // Verify that the request belongs to the student
        if (!request.getStudent().getId().equals(studentId)) {
            throw new UnauthorizedException("You can only cancel your own requests");
        }

        // Can only cancel if status is REQUESTED or APPROVED
        if (request.getStatus() != BUS_REQUEST_STATUS.REQUESTED && 
            request.getStatus() != BUS_REQUEST_STATUS.APPROVED) {
            throw new IllegalStateException("Cannot cancel request with status: " + request.getStatus());
        }

        request.setStatus(BUS_REQUEST_STATUS.CANCELLED);
        return toResponse(busRequestRepository.save(request));
    }

    // ── Delete Request (Admin only) ────────────────────────────────────────
    @Transactional
    public void deleteRequest(Long id) {
        if (!busRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bus request not found with id: " + id);
        }
        busRequestRepository.deleteById(id);
    }

    // ── Get Requests by Route ──────────────────────────────────────────────
    public List<BusRequestResponse> getRequestsByRoute(Long routeId) {
        return busRequestRepository.findByRouteId(routeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Requests by Pickup Point ───────────────────────────────────────
    public List<BusRequestResponse> getRequestsByPickupPoint(Long pickupPointId) {
        return busRequestRepository.findByPickupPointId(pickupPointId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Get Statistics ─────────────────────────────────────────────────────
    public BusRequestStatistics getStatistics() {
        return BusRequestStatistics.builder()
                .totalRequests(busRequestRepository.count())
                .pendingRequests(busRequestRepository.countByStatus(BUS_REQUEST_STATUS.REQUESTED))
                .approvedRequests(busRequestRepository.countByStatus(BUS_REQUEST_STATUS.APPROVED))
                .rejectedRequests(busRequestRepository.countByStatus(BUS_REQUEST_STATUS.REJECTED))
                .activeRequests(busRequestRepository.findActiveRequests(LocalDate.now()).size())
                .build();
    }

    // ── Helper Methods ─────────────────────────────────────────────────────
    private BusRequest getOrThrow(Long id) {
        return busRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus request not found with id: " + id));
    }

    private BusRequestResponse toResponse(BusRequest request) {
        return BusRequestResponse.builder()
                .id(request.getId())
                .student(mapToStudentResponse(request.getStudent()))
                .route(mapToRouteResponse(request.getRoute()))
                .pickupPoint(mapToPickupPointResponse(request.getPickupPoint()))
                .requestedFrom(request.getRequestedFrom())
                .requestedTo(request.getRequestedTo())
                .reason(request.getReason())
                .status(request.getStatus())
                .adminRemarks(request.getAdminRemarks())
                .approvalDate(request.getApprovalDate())
                .approvedBy(request.getApprovedBy())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .isActive(request.isActive())
                .build();
    }

    private BusRequestSummaryResponse toSummaryResponse(BusRequest request) {
        return BusRequestSummaryResponse.builder()
                .id(request.getId())
                .studentName(request.getStudent().getName())
                .studentId(request.getStudent().getStudentId())
                .studentEmail(request.getStudent().getEmail())
                .routeName(request.getRoute().getRouteName())
                .busNo(request.getRoute().getBusNo())
                .pickupPointName(request.getPickupPoint().getPlaceName())
                .requestedFrom(request.getRequestedFrom())
                .requestedTo(request.getRequestedTo())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .build();
    }

    private StudentResponse mapToStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .department(student.getDepartment())
                .batch(student.getBatch())
                .gender(student.getGender())
                .shift(student.getShift())
                .build();
    }

    private com.fmahadybd.bms_services.route.dto.RouteResponse mapToRouteResponse(Route route) {
        return com.fmahadybd.bms_services.route.dto.RouteResponse.builder()
                .id(route.getId())
                .busNo(route.getBusNo())
                .routeName(route.getRouteName())
                .routeLine(route.getRouteLine())
                .status(route.getStatus())
                .build();
    }

    private com.fmahadybd.bms_services.route.dto.PickupPointResponse mapToPickupPointResponse(PickupPoint pickupPoint) {
        return com.fmahadybd.bms_services.route.dto.PickupPointResponse.builder()
                .id(pickupPoint.getId())
                .placeName(pickupPoint.getPlaceName())
                .placeDetails(pickupPoint.getPlaceDetails())
                .pickupTime(pickupPoint.getPickupTime())
                .stopOrder(pickupPoint.getStopOrder())
                .build();
    }
}