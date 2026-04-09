package com.leonardo.delivery_tracking_system.dto.address;

import com.leonardo.delivery_tracking_system.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

public record AddressUpdateDTO(
        @NotBlankIfPresent @Size(max = 255) String street,
        @NotBlankIfPresent @Size(max = 10) String number,
        @NotBlankIfPresent @Size(max = 15) String zipCode,
        @NotBlankIfPresent @Size(max = 100) String city,
        @NotBlankIfPresent @Size(max = 100) String neighborhood) {}
