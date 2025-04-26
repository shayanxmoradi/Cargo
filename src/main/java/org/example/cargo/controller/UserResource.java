package org.example.cargo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Validated UserCreateDto user) {
        UserResponseDto userResponseDto = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

    }
}
