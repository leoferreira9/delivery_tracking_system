package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, DelivererMapper.class, EstablishmentMapper.class, AddressMapper.class})
public interface DeliveryMapper {

    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.phone", target = "customerPhone")
    @Mapping(source = "deliverer.name", target = "delivererName")
    @Mapping(source = "deliverer.vehicleType", target = "vehicleType")
    @Mapping(source = "establishment.name", target = "establishmentName")
    @Mapping(source = "customer.address", target = "address")
    DeliveryResponse toDto(Delivery delivery);
}
