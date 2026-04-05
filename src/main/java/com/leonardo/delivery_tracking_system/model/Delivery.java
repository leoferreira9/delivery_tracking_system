package com.leonardo.delivery_tracking_system.model;

import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 15)
    private String trackingCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Deliverer deliverer;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Establishment establishment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
