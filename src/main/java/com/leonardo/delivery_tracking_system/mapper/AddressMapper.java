package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.address.AddressRequest;
import com.leonardo.delivery_tracking_system.dto.address.AddressResponse;
import com.leonardo.delivery_tracking_system.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toDto(Address address);

    Address toEntity(AddressRequest request);
}
