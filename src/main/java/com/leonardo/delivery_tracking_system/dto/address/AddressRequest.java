package com.leonardo.delivery_tracking_system.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank @Size(max = 255) String street,
        @NotBlank @Size(max = 10) String number,
        @NotBlank @Size(max = 15) String zipCode,
        @NotBlank @Size(max = 100) String city,
        @NotBlank @Size(max = 100) String neighborhood
) {}
