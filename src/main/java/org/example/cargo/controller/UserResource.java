package org.example.cargo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cargo.dto.PasswordUpdateDto;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(user);
        UserResponseDto userResponseDto = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

    }

    @PatchMapping("/{id}/password") // Using PATCH is common for partial updates like this
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Validated PasswordUpdateDto passwordUpdateDto) {
        // Add logic to validate ID matches DTO ID, verify current password,
        // check new and confirm passwords, hash the new password, and save.
        //todo
        // userService.updatePassword(id, passwordUpdateDto);
        return ResponseEntity.noContent().build(); // Or return a success message
    }
}
