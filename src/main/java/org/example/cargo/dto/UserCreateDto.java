package org.example.cargo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.With;
import org.hibernate.validator.constraints.Length;

public record UserCreateDto(
        @With Long id,
        @Length(min = 3, max = 30, message = "should be greater than 3 and less than 30")
        @NotBlank(message = "first name is required")
        String firstName,
        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "you should enter valid email address")
        String email,
        @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String password,
        @NotBlank(message = "username is required")
        @Length(min = 3, max = 30, message = "should be greater than 3 and less than 30")
        String username) {

}
