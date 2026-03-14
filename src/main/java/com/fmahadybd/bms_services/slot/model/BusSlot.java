package com.fmahadybd.bms_services.slot.model;

import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bus_slots", indexes = {
    @Index(name = "idx_bus_slot_route", columnList = "route_id"),
    @Index(name = "idx_bus_slot_status", columnList = "status"),
    @Index(name = "idx_bus_slot_time", columnList = "pickup_time")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BusSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false, length = 100)
    private String slotName;

    @Column(nullable = false)
    private LocalTime pickupTime;

    @Column
    private LocalTime dropTime;

    @Column(nullable = false)
    private String fromLocation;

    @Column(nullable = false)
    private String toLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BUS_SLOT_STATUS status;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean isRegular = true;

    @Column(length = 50)
    private String regularDays;

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