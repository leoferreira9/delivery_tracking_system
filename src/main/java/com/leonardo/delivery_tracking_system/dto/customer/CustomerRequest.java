package com.leonardo.delivery_tracking_system.dto.customer;

import com.leonardo.delivery_tracking_system.dto.address.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotNull @Size(max = 255) String name,
        @NotNull @Size(max = 16) String cpf,
        @NotNull @Size(max = 20) String phone,
        @NotNull @Email String email,
        @NotNull AddressRequest address
) {}