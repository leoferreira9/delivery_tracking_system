package com.leonardo.delivery_tracking_system.dto.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotNull @Size(max = 255) String street,
        @NotNull @Size(max = 10) String number,
        @NotNull @Size(max = 15) String zipCode,
        @NotNull @Size(max = 100) String city,
        @NotNull @Size(max = 100) String neighborhood
) {}
