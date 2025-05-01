package org.example.cargo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.With;
import org.hibernate.validator.constraints.Length;
@Data
public class UserPatchDto{
    // ID is typically provided in the URL path for PATCH, not in the body.

    @Length(min = 3, max = 30, message = "First name length must be between 3 and 30 if provided")
    private String firstName; // If null, don't update firstName

    // No validation needed on lastName other than perhaps length if desired
    @Length(max = 50, message = "Last name cannot exceed 50 characters if provided") // Example length limit
    private String lastName; // If null, don't update lastName

    @Email(message = "Must be a valid email address if provided")
    private String email; // If null, don't update email

    @Length(min = 3, max = 30, message = "Username length must be between 3 and 30 if provided")
    private String username; // If null, don't update username


}