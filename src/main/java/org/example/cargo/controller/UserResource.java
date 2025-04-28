package org.example.cargo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cargo.dto.PasswordUpdateDto;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Validated UserCreateDto userCreateDto) {
        UserResponseDto createdUser = userService.save(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody @Validated UserUpdateDto updateDto) {
        if (!id.equals(updateDto.id())) {
            return ResponseEntity.badRequest().build(); // ID mismatch
        }
        UserResponseDto updatedUser = userService.update(updateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/{id}/password")
//    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Validated PasswordUpdateDto passwordUpdateDto) {
//        if (!id.equals(passwordUpdateDto.id())) {
//            return ResponseEntity.badRequest().build(); // ID mismatch
//        }
//        userService.updatePassword(id, passwordUpdateDto);
//        return ResponseEntity.noContent().build();
//    }
}