package org.example.cargo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.domain.user.User;
import org.example.cargo.service.UserService;
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
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Validated UserCreateDto user) {


        Optional<UserCreateDto> userRequestDtoOptional = userService.addUser(user);
    }
}
