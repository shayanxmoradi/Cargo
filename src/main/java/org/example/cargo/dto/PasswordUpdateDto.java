package org.example.cargo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDto(
        @NotNull(message = "User Id Cannot be null for password update")
        Long id,
        @NotBlank(message = "current pasword cannot be blank")
        String currentPassword,
        @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String newPassword
) {
}
