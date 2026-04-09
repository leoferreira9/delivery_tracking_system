package com.leonardo.delivery_tracking_system.dto.customer;

import com.leonardo.delivery_tracking_system.dto.address.AddressUpdateDTO;
import com.leonardo.delivery_tracking_system.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CustomerUpdateDTO(
        @NotBlankIfPresent @Size(max = 255) String name,
        @NotBlankIfPresent @Size(max = 16) String cpf,
        @NotBlankIfPresent @Size(max = 20) String phone,
        @NotBlankIfPresent @Email String email,
        AddressUpdateDTO address) {}
