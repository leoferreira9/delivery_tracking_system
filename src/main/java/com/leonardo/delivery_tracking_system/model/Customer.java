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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 255)
    private String name;

    @Column(nullable = false, unique = true)
    @Size(max = 16)
    private String cpf;

    @Column(nullable = false)
    @Size(max = 20)
    private String phone;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    private String email;

    @Embedded
    @Column(nullable = false)
    private Address address;
}
