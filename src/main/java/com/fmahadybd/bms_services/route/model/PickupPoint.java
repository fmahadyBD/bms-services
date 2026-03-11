
package com.fmahadybd.bms_services.route.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "pickup_points")
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
    private String placeName;       // e.g. "Mirpur 10", "Farmgate"

    @Column(length = 255)
    private String placeDetails;    // e.g. "In front of Mirpur 10 Metro Station"

    @Column(nullable = false)
    private LocalTime pickupTime;   // e.g. 07:30

    @Column(nullable = false)
    private Integer stopOrder;      // 1, 2, 3... for ordering stops
}