package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryStatusHistoryResponse;
import com.leonardo.delivery_tracking_system.model.DeliveryStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryStatusHistoryMapper {
    @Mapping(source = "delivery.id", target = "deliveryId")
    DeliveryStatusHistoryResponse toDto(DeliveryStatusHistory deliveryStatusHistory);
}
