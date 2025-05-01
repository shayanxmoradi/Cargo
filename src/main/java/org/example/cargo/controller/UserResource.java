package org.example.cargo.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserPatchDto;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.exception.ResourceNotFoundException;
import org.example.cargo.service.UserService;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Cargo app", version = "1.0", description = "here is overal description of my APIs"))
public class UserResource {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "its just summary for Documentation")
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

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user (full update)") // Example operation summary
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Validated UserUpdateDto updateDto) {
        log.info("Request to update user with id: {}, data: {}", id, updateDto); // Example logging
        // Validation ensures the incoming DTO is complete and valid.
        try {
            // *** CORRECTED ARGUMENT ORDER HERE ***
            UserResponseDto updatedUser = userService.update(id, updateDto);
            log.info("User updated successfully: {}", updatedUser);
            return ResponseEntity.ok(updatedUser); // 200 OK
        } catch (ResourceNotFoundException e) {
            log.warn("Failed to update user. User not found with id: {}", id, e); // Example logging
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        // Consider handling validation exceptions globally (@ControllerAdvice)
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> patchUser(
            @PathVariable Long id,
            @RequestBody @Validated UserPatchDto patchDto) { // Use @Validated

        try {
            // Delegate the patch logic entirely to the service layer
            UserResponseDto updatedUser = userService.patch(id, patchDto);
            return ResponseEntity.ok(updatedUser); // 200 OK with updated user

        } catch (ResourceNotFoundException e) {
            // Let the global exception handler (or @ResponseStatus on exception) handle this
            // Or return explicitly: return ResponseEntity.notFound().build();
            throw e; // Re-throw for global handler is often cleaner
        }
        // Other exceptions (like validation) should ideally be handled by a global handler
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}