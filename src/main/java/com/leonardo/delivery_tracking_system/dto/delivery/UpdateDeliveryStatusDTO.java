package com.leonardo.delivery_tracking_system.dto.delivery;

import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryStatusDTO(@NotNull DeliveryStatus status) {}
