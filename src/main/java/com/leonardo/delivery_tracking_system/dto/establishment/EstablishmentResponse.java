package com.leonardo.delivery_tracking_system.dto.establishment;

import com.leonardo.delivery_tracking_system.dto.address.AddressResponse;

public record EstablishmentResponse(
    Long id,
    String name,
    AddressResponse address
) {}
