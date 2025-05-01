package org.example.cargo.service;

import org.example.cargo.domain.user.User;
import org.example.cargo.dto.*;
import org.example.cargo.exception.ResourceNotFoundException;
import org.example.cargo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl extends CrudServiceImpl<Long, User, UserResponseDto, UserCreateDto, UserUpdateDto> implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    protected User mapCreateDtoToEntity(UserCreateDto createDto) {
        return userMapper.fromCreateDto(createDto);
    }

    @Override
    protected User mapUpdateDtoToEntity(UserUpdateDto updateDto, User existingEntity) {
        userMapper.updateUserFromDto(updateDto, existingEntity);
        return existingEntity;
    }

    @Override
    protected UserResponseDto mapEntityToResponseDto(User entity) {
        return userMapper.toResponseDto(entity);
    }



    @Override
    public Optional<UserResponseDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponseDto);
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }


    @Override
    @Transactional // Ensure the read-modify-write operation is atomic
    public UserResponseDto patch(Long id, UserPatchDto patchDto) throws ResourceNotFoundException {
        // 1. Fetch the existing entity
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // 2. Conditionally apply updates from the DTO
        boolean updated = false; // Optional: track if any changes were made

        if (patchDto.getFirstName() != null) {
            // Optional: Add validation here if 'firstName' must not be blank when provided
            // if (patchDto.getFirstName().isBlank()) { throw new IllegalArgumentException("First name cannot be blank"); }
            existingUser.setFirstName(patchDto.getFirstName());
            updated = true;
        }
        if (patchDto.getLastName() != null) {
            existingUser.setLastName(patchDto.getLastName());
            updated = true;
        }
        if (patchDto.getEmail() != null) {
            // Optional: Add validation for email format if not covered by @Email on DTO
            // Optional: Check for email uniqueness if required
            existingUser.setEmail(patchDto.getEmail());
            updated = true;
        }
        if (patchDto.getUsername() != null) {
            // Optional: Add validation for username format/length if needed
            // Optional: Check for username uniqueness if required
            existingUser.setUsername(patchDto.getUsername());
            updated = true;
        }

        // Add checks for other patchable fields...

        // 3. Save the entity (only if changes were made, optional optimization)
        User savedUser = existingUser; // Default to existing if no updates
        if (updated) {
            savedUser = userRepository.save(existingUser);
        }

        // 4. Map the potentially updated entity to the response DTO
        return userMapper.toResponseDto(savedUser);
    }
}
