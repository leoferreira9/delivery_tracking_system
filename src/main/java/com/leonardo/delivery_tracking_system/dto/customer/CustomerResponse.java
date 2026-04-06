package com.leonardo.delivery_tracking_system.dto.customer;

import com.leonardo.delivery_tracking_system.dto.address.AddressResponse;

public record CustomerResponse(
        Long id,
        String name,
        String phone,
        String email,
        AddressResponse address
) {}
