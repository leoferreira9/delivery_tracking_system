package com.leonardo.delivery_tracking_system.dto.customer;

import com.leonardo.delivery_tracking_system.dto.address.AddressRequest;
import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 16) @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$") String cpf,
        @NotBlank @Size(max = 20) String phone,
        @NotBlank @Email String email,
        @NotNull AddressRequest address
) {}