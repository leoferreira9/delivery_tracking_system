package com.leonardo.delivery_tracking_system.dto.deliverer;

import com.leonardo.delivery_tracking_system.enums.VehicleType;

public record DelivererResponse(
        Long id,
        String name,
        String phone,
        VehicleType vehicleType
) {}
