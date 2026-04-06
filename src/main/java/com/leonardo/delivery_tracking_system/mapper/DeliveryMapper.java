package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryRequest;
import com.leonardo.delivery_tracking_system.dto.delivery.DeliveryResponse;
import com.leonardo.delivery_tracking_system.model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class, DelivererMapper.class, EstablishmentMapper.class})
public interface DeliveryMapper {

    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.phone", target = "customerPhone")
    @Mapping(source = "deliverer.name", target = "delivererName")
    @Mapping(source = "establishment.name", target = "establishmentName")
    DeliveryResponse toDto(Delivery delivery);


    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "establishmentId", target = "establishment.id")
    Delivery toEntity(DeliveryRequest request);
}
