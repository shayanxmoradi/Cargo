package org.example.cargo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.With;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDto(
                            @Length(min = 3, max = 30, message = "should be greater than 3 and less than 30")
                            @NotBlank(message = "first name is required")
                            String firstName,
                            String lastName,
                            @NotBlank(message = "email is required")
                            @Email(message = "you should enter valid email address")
                            String email,
                            @NotBlank(message = "username is required")
                            @Length(min = 3, max = 30, message = "should be greater than 3 and less than 30")
                            String username) {
}
