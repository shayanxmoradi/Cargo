package org.example.cargo.dto;

import org.example.cargo.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;



@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true) // ignore createdAt
    @Mapping(target = "updatedAt", ignore = true) // ignore updatedAt
    User fromCreateDto(UserCreateDto dto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

    UserResponseDto toResponseDto(User user);
}
