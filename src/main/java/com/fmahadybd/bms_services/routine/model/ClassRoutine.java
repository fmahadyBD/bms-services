package com.fmahadybd.bms_services.routine.model;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.enums.ROUTINE_TYPE;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.student.model.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_routines", indexes = {
    @Index(name = "idx_routine_day", columnList = "day"),
    @Index(name = "idx_routine_batch", columnList = "batch,department")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClassRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String courseName;

    @Column(nullable = false, length = 20)
    private String courseCode;

    @Column(nullable = false, length = 50)
    private String teacherName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DAY day;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 100)
    private String roomNumber;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 20)
    private String batch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROUTINE_TYPE routineType = ROUTINE_TYPE.CLASS;

    @Column(nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;  // Optional: if class needs bus route info

    @ManyToMany
    @JoinTable(
        name = "routine_students",
        joinColumns = @JoinColumn(name = "routine_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}