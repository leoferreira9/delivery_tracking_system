package com.leonardo.delivery_tracking_system.dto.delivery;

import jakarta.validation.constraints.NotNull;

public record DeliveryRequest(
        @NotNull Long customerId,
        @NotNull Long establishmentId
) {}
