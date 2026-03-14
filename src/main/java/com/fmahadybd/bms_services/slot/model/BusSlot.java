package com.fmahadybd.bms_services.slot.model;



import com.fmahadybd.bms_services.route.model.Route;
import com.fmahadybd.bms_services.slot.emnus.BUS_SLOT_STATUS;
import com.fmahadybd.bms_services.route.model.PickupPoint;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private PickupPoint pickupPoint;

    @Column(nullable = false, length = 100)
    private String slotName;           // e.g., "Morning Slot", "Evening Slot"

    @Column(nullable = false)
    private LocalTime pickupTime;       // e.g., 07:30 AM

    @Column
    private LocalTime dropTime;         // e.g., 05:30 PM

    @Column(nullable = false)
    private Integer maxCapacity;        // Maximum students allowed

    @Column(nullable = false)
    private Integer currentBookings = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BUS_SLOT_STATUS status;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean isRecurring = false;

    @Column(length = 50)
    private String recurringDays;       // e.g., "MONDAY,WEDNESDAY,FRIDAY"

    @Column
    private LocalTime cutoffTime;       // Last time to book

    @Column(nullable = false)
    private Integer bufferMinutes = 15;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Double fareAmount;

    @Column(length = 20)
    private String vehicleNumber;

    @Column(length = 50)
    private String driverName;

    @Column(length = 15)
    private String driverPhone;

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

    // Helper methods
    public boolean isAvailable() {
        return status == BUS_SLOT_STATUS.ACTIVE && currentBookings < maxCapacity;
    }

    public int getAvailableSeats() {
        return maxCapacity - currentBookings;
    }

    public void incrementBooking() {
        if (currentBookings < maxCapacity) {
            currentBookings++;
            if (currentBookings >= maxCapacity) {
                status = BUS_SLOT_STATUS.FULL;
            }
        }
    }

    public void decrementBooking() {
        if (currentBookings > 0) {
            currentBookings--;
            if (status == BUS_SLOT_STATUS.FULL && currentBookings < maxCapacity) {
                status = BUS_SLOT_STATUS.ACTIVE;
            }
        }
    }
}