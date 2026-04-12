package com.leonardo.delivery_tracking_system.dto.deliverer;

import com.leonardo.delivery_tracking_system.enums.VehicleType;
import com.leonardo.delivery_tracking_system.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Size;

public record DelivererUpdateRequest(
        @NotBlankIfPresent @Size(max = 255) String name,
        @NotBlankIfPresent @Size(max = 20)String phone,
        VehicleType vehicleType) {}
