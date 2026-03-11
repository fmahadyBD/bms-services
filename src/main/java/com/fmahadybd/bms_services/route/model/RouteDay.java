
package com.fmahadybd.bms_services.route.model;

import com.fmahadybd.bms_services.enums.DAY;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route_days")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RouteDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DAY day;
}