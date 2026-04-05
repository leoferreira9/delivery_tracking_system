package com.leonardo.delivery_tracking_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @Column(nullable = false)
    @Size(max = 255)
    private String cnpj;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    @Size(max = 20)
    private String phone;
}
