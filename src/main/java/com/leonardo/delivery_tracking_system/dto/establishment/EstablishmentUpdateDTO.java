package com.leonardo.delivery_tracking_system.dto.establishment;

import com.leonardo.delivery_tracking_system.dto.address.AddressUpdateDTO;
import com.leonardo.delivery_tracking_system.validation.NotBlankIfPresent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EstablishmentUpdateDTO(
        @NotBlankIfPresent @Size(max = 255) String name,
        @NotBlankIfPresent @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$") String cnpj,
        @NotBlankIfPresent @Size(max = 20) String phone,
        @Valid AddressUpdateDTO address) {}
