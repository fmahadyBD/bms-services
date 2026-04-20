package com.fmahadybd.bms_services.survey.model;

import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "surveys", indexes = {
        @Index(name = "idx_survey_status", columnList = "status"),
        @Index(name = "idx_survey_date_range", columnList = "start_date,end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurveyStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private String semester;

    @Column(name = "total_responses")
    private Integer totalResponses;

    @Column(name = "target_responses")
    private Integer targetResponses;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SurveyQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SurveyResponse> responses = new ArrayList<>();

    // NEW: Available routes for this survey
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "survey_routes", joinColumns = @JoinColumn(name = "survey_id"), inverseJoinColumns = @JoinColumn(name = "route_id"))
    private List<Route> availableRoutes = new ArrayList<>();

    // NEW: Available time slots for this survey
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "survey_slots", joinColumns = @JoinColumn(name = "survey_id"), inverseJoinColumns = @JoinColumn(name = "slot_id"))
    private List<BusSlot> availableSlots = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    // Add to Survey.java entity
    @Column(columnDefinition = "json")
    private Map<String, Object> metadata;
}