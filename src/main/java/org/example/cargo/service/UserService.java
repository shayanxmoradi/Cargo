package org.example.cargo.service;

import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserPatchDto;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.exception.ResourceNotFoundException;

import java.util.Optional;

public interface UserService extends CrudService<Long, UserResponseDto, UserCreateDto, UserUpdateDto> {
    Optional<UserResponseDto> findByUsername(String username);
    UserResponseDto patch(Long id, UserPatchDto patchDto) throws ResourceNotFoundException;

}
