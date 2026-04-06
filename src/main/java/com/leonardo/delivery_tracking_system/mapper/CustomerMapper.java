package com.leonardo.delivery_tracking_system.mapper;

import com.leonardo.delivery_tracking_system.dto.customer.CustomerRequest;
import com.leonardo.delivery_tracking_system.dto.customer.CustomerResponse;
import com.leonardo.delivery_tracking_system.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CustomerMapper {

    CustomerResponse toDto(Customer customer);

    Customer toEntity(CustomerRequest request);
}
