package org.example.cargo.dto;

public record UserResponseDto(  Long id,
                                String firstName,
                                String lastName,
                                String email,
                                String username) {
}
