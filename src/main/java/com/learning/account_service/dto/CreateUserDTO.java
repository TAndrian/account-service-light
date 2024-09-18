package com.learning.account_service.dto;

import com.learning.account_service.business.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank(message = "Firstname must be provided.")
        String firstname,
        @NotBlank(message = "Lastname must be provided.")
        String lastname,
        @NotBlank(message = "Email must be provided.")
        String email,
        @NotBlank(message = "Password must be provided.")
        String password,
        @NotBlank(message = "Role must be provided.")
        Role role
) {
}
