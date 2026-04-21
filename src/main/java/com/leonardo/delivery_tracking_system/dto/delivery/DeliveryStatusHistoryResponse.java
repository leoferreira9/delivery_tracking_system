package com.leonardo.delivery_tracking_system.dto.delivery;

import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryStatusHistoryResponse(Long deliveryId, DeliveryStatus status, LocalDateTime changedAt) {}
