package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererRequest;
import com.leonardo.delivery_tracking_system.dto.deliverer.DelivererResponse;
import com.leonardo.delivery_tracking_system.model.Deliverer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DelivererMapper {

    DelivererResponse toDto(Deliverer deliverer);

    Deliverer toEntity(DelivererRequest request);
}
