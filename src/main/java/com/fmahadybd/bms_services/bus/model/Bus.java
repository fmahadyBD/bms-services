package com.fmahadybd.bms_services.bus.model;

import com.fmahadybd.bms_services.bus.enums.BUS_STATUS;
import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.model.BusSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "buses", indexes = {
    @Index(name = "idx_bus_number", columnList = "busNumber", unique = true),
    @Index(name = "idx_bus_status", columnList = "status")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String busName;           // e.g. "Green Line AC", "Hanif Express"

    @Column(nullable = false, unique = true, length = 20)
    private String busNumber;         // e.g. "BUS-001", "DHK-1234"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BUS_STATUS status;

    @Column(nullable = false, length = 100)
    private String driverName;

    @Column(nullable = false, length = 100)
    private String helperName;

    @Column(nullable = false, length = 15)
    private String driverPhone;

    @Column(nullable = false, length = 15)
    private String helperPhone;

    // Relationship with Route (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    // Relationship with BusSlot (One-to-Many)
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BusSlot> busSlots;

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