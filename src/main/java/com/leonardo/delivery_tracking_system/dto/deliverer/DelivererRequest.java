package com.leonardo.delivery_tracking_system.dto.deliverer;

import com.leonardo.delivery_tracking_system.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DelivererRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 20) String phone,
        @NotNull VehicleType vehicleType
) {}
