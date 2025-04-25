package org.example.cargo.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl extends CrudServiceImpl<Long, User, UserResponseDto, UserCreateDto, UserUpdateDto>implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    protected User mapCreateDtoToEntity(UserCreateDto createDto) {
        return null;
    }

    @Override
    protected User mapUpdateDtoToEntity(UserUpdateDto updateDto, User existingEntity) {
        return null;
    }

    @Override
    protected UserResponseDto mapEntityToResponseDto(User entity) {
        return null;
    }

    @Override
    protected Long extractIdFromUpdateDto(UserUpdateDto updateDto) {
        return 0l;
    }

    @Override
    public Optional<UserResponseDto> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return null;
    }
}
