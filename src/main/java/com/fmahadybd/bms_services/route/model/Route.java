package com.fmahadybd.bms_services.route.model;

import com.fmahadybd.bms_services.bus.model.Bus;
import com.fmahadybd.bms_services.enums.ROUTE_STATUS;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes", indexes = {
    @Index(name = "idx_route_bus_no", columnList = "busNo", unique = true),
    @Index(name = "idx_route_status", columnList = "status")
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
    private String busNo;

    @Column(nullable = false, length = 100)
    private String routeName;

    @Column(nullable = false, length = 255)
    private String routeLine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ROUTE_STATUS status = ROUTE_STATUS.ACTIVE;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("stopOrder ASC")
    @Builder.Default
    private List<PickupPoint> pickupPoints = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RouteDay> operatingDays = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Bus> buses = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BusSlot> busSlots = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private Long createdBy;

    @Column
    private Long updatedBy;
}