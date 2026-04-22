package com.leonardo.delivery_tracking_system.dto.establishment;

import com.leonardo.delivery_tracking_system.dto.address.AddressRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EstablishmentRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$") String cnpj,
        @NotBlank @Size(max = 20) String phone,
        @NotNull AddressRequest address
) {}
