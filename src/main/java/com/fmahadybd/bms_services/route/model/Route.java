
package com.fmahadybd.bms_services.route.model;

import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routes", indexes = {
    @Index(name = "idx_route_bus_no", columnList = "busNo", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String busNo;           // e.g. "BUS-01", "BUS-12"

    @Column(nullable = false, length = 100)
    private String routeName;       // e.g. "Mirpur - BUET"

    @Column(nullable = false, length = 255)
    private String routeLine;       // e.g. "Mirpur 10 → Farmgate → Shahbag → BUET"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROUTE_STATUS status = ROUTE_STATUS.ACTIVE;

    // ── Pickup Points (One Route → Many Pickups) ──────────────────────────
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("pickupTime ASC")
    private List<PickupPoint> pickupPoints;

    // ── Operating Days (One Route → Many Days) ────────────────────────────
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteDay> operatingDays;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}