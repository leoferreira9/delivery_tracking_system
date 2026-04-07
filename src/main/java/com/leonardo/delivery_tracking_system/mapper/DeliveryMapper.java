package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.model.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryResponse toDto(Delivery delivery);
}
