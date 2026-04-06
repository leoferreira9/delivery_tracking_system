package com.leonardo.delivery_tracking_system.dto.establishment;

import com.leonardo.delivery_tracking_system.dto.address.AddressRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstablishmentRequest(
        @NotNull @Size(max = 255) String name,
        @NotNull @Size(max = 255) String cnpj,
        @NotNull @Size(max = 20) String phone,
        @NotNull AddressRequest address
) {}
