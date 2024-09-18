package com.learning.account_service.dto;

public record UpdateUserDTO(
        String firstname,
        String lastname,
        String email
) {
}
