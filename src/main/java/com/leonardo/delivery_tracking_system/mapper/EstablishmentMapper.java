package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentRequest;
import com.leonardo.delivery_tracking_system.dto.establishment.EstablishmentResponse;
import com.leonardo.delivery_tracking_system.model.Establishment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface EstablishmentMapper {

    EstablishmentResponse toDto(Establishment establishment);

    Establishment toEntity(EstablishmentRequest request);
}
