package com.fmahadybd.bms_services.route.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "pickup_points", indexes = {
    @Index(name = "idx_pickup_route", columnList = "route_id"),
    @Index(name = "idx_pickup_time", columnList = "pickup_time")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false, length = 100)
    private String placeName;

    @Column(length = 255)
    private String placeDetails;

    @Column(nullable = false)
    private LocalTime pickupTime;

    @Column(nullable = false)
    private Integer stopOrder;
}