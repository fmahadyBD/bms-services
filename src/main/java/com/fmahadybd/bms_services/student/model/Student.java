package com.fmahadybd.bms_services.student.model;

import com.fmahadybd.bms_services.enums.GENDER;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.routine.model.ClassRoutine;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_id", columnList = "studentId", unique = true),
        @Index(name = "idx_student_email", columnList = "email", unique = true),
        @Index(name = "idx_student_phone", columnList = "phoneNumber", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String studentId; // e.g. "2210024**"

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 20)
    private String batch; // e.g. "221"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GENDER gender;

    @Column(nullable = false)
    private boolean isBlocked = false;

    @Column(nullable = false, length = 20)
    private String shift; // e.g. , "Day", "Evening"

    @Column(nullable = false)
    private String password; // BCrypt hashed

    // Many-to-One with Route (Many students can use the same route)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    // Many-to-Many with ClassRoutine
    @ManyToMany(mappedBy = "students")
    private List<ClassRoutine> routines = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}