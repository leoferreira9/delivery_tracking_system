package com.leonardo.delivery_tracking_system.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String neighborhood;
}
