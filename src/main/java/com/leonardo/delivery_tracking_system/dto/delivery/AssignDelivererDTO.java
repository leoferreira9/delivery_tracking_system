package com.leonardo.delivery_tracking_system.dto.delivery;

import jakarta.validation.constraints.NotNull;

public record AssignDelivererDTO(@NotNull Long delivererId) {}
