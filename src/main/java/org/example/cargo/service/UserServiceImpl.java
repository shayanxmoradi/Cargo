package org.example.cargo.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserMapper;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserServiceImpl extends CrudServiceImpl<Long, User, UserResponseDto, UserCreateDto, UserUpdateDto>implements UserService {
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
         userMapper.updateUserFromDto(updateDto,existingEntity);
         return existingEntity;
    }

    @Override
    protected UserResponseDto mapEntityToResponseDto(User entity) {
        return userMapper.toResponseDto(entity);
    }

    @Override
    protected Long extractIdFromUpdateDto(UserUpdateDto updateDto) {
        return updateDto.id();
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
}
