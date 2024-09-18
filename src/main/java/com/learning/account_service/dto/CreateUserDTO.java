package com.learning.account_service.dto;

import com.learning.account_service.business.enums.Role;
import jakarta.validation.constraints.NotEmpty;

public record CreateUserDTO(
        @NotEmpty(message = "Firstname is required.")
        String firstname,
        @NotEmpty(message = "Lastname is required.")
        String lastname,
        @NotEmpty(message = "Email is required.")
        String email,
        @NotEmpty(message = "Password is required.")
        String password,
        Role role
) {
}
