package com.leonardo.delivery_tracking_system.dto.establishment;

import com.leonardo.delivery_tracking_system.dto.address.AddressUpdateDTO;
import com.leonardo.delivery_tracking_system.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

public record EstablishmentUpdateDTO(
        @NotBlankIfPresent @Size(max = 255) String name,
        @NotBlankIfPresent @Size(max = 255) String cnpj,
        @NotBlankIfPresent @Size(max = 20) String phone,
        AddressUpdateDTO address) {}
