package com.leonardo.delivery_tracking_system.dto.delivery;

import com.leonardo.delivery_tracking_system.dto.address.AddressResponse;
import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.enums.VehicleType;

import java.time.LocalDateTime;

public record DeliveryResponse(
    Long id,
    String trackingCode,
    DeliveryStatus status,
    String customerName,
    String customerPhone,
    String delivererName,
    VehicleType vehicleType,
    String establishmentName,
    AddressResponse address,
    LocalDateTime createdAt,
    LocalDateTime deliveredAt
) {}
