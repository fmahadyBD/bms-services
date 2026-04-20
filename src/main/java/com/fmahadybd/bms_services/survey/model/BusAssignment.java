// com/fmahadybd/bms_services/survey/model/BusAssignment.java - Fixed imports
package com.fmahadybd.bms_services.survey.model;

import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.student.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bus_assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BusAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    
    private String studentIdentifier; // studentId or email
    
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route assignedRoute;
    
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private BusSlot assignedSlot;
    
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus assignedBus;
    
    @Enumerated(EnumType.STRING)
    private DAY assignedDay;
    
    private String boardingPoint;
    private String dropPoint;
    private String pickupTime;
    
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;
    
    private String rejectionReason;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime assignedAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private Long assignedBy; // manager/admin ID
}