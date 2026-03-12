package com.fmahadybd.bms_services.manager.model;

import com.fmahadybd.bms_services.auth.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "managers", indexes = {
        @Index(name = "idx_manager_email", columnList = "email", unique = true),
        @Index(name = "idx_manager_phone", columnList = "phoneNumber", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Manager implements BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String managerId;

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

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private boolean isBlocked = false;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public String getUserType() {
        return "MANAGER";
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
        return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
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