package com.fmahadybd.bms_services.student.model;

import com.fmahadybd.bms_services.auth.BaseUser;
import com.fmahadybd.bms_services.enums.GENDER;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.routine.model.ClassRoutine;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
public class Student implements BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 50) 
    private String studentId;

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
    private String batch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GENDER gender;

    @Column(nullable = false)
    private boolean isBlocked = false;

    @Column(nullable = false, length = 20)
    private String shift;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToMany(mappedBy = "students")
    private List<ClassRoutine> routines = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public String getUserType() {
        return "STUDENT";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isBlocked;
    }

    @Override
    public String getEmail() {
        return email;
    }
}