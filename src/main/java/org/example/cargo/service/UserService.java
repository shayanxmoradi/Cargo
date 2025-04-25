package org.example.cargo.service;

import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;

import java.util.Optional;

public interface UserService extends CrudService<Long, UserResponseDto, UserCreateDto, UserUpdateDto> {
    Optional<UserResponseDto> findByUsername(String username);

}
