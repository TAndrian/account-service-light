package com.learning.account_service.dto;

import com.learning.account_service.business.enums.Role;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstname,
        String lastname,
        String email,
        Role role
) {
}
