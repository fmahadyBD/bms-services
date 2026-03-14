package com.fmahadybd.bms_services.new_bus_request.model;

import com.fmahadybd.bms_services.student.model.Student;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.new_bus_request.enums.BUS_REQUEST_STATUS;
import com.fmahadybd.bms_services.route.model.PickupPoint;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_requests", indexes = {
    @Index(name = "idx_bus_request_student", columnList = "student_id"),
    @Index(name = "idx_bus_request_status", columnList = "status"),
    @Index(name = "idx_bus_request_created_at", columnList = "createdAt")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BusRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private PickupPoint pickupPoint;

    @Column(nullable = false)
    private LocalDate requestedFrom;  // Date from which student needs bus service

    @Column(nullable = false)
    private LocalDate requestedTo;    // Date until which student needs bus service

    @Column(length = 500)
    private String reason;            // Reason for bus request

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BUS_REQUEST_STATUS status;

    @Column(length = 500)
    private String adminRemarks;      // Admin remarks for approval/rejection

    @Column
    private LocalDate approvalDate;   // When the request was approved

    @Column
    private Long approvedBy;          // Admin ID who approved/rejected

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Helper method to check if request is active
    public boolean isActive() {
        return status == BUS_REQUEST_STATUS.APPROVED && 
               LocalDate.now().isBefore(requestedTo) && 
               LocalDate.now().isAfter(requestedFrom.minusDays(1));
    }
}