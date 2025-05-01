package org.example.cargo.service;

import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserMapper;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserMapper mockUserMapper;
    @InjectMocks
    private UserServiceImpl userService;


    private User sampleUser;
    private UserResponseDto sampleResponseDto;
    private final Long testUserId = 1L;

    @BeforeEach
    void setUpTestData() {
        sampleUser = new User();
        sampleUser.setId(testUserId);
        sampleUser.setUsername("testuser");
        sampleUser.setFirstName("Test");
        sampleUser.setLastName("User");
        sampleUser.setEmail("test@example.com");

        sampleResponseDto = new UserResponseDto(
                testUserId,
                "Test",
                "User",
                "test@example.com",
                "testuser"
        );
    }
    @Test
    void setup_shouldInjectMocks(){
        assertNotNull(userService,"UserServiceImpl instance should be created");
        assertNotNull(mockUserMapper,"UserMapper instance should be created");
        assertNotNull(mockUserRepository,"UserRepository instance should be created");

    }

    @Test
    void findById_whenUserExists_shouldReturnUserDtoOptional(){
        when(mockUserRepository.findById(testUserId)).thenReturn(Optional.of(sampleUser));
        when(mockUserMapper.toResponseDto(sampleUser)).thenReturn(sampleResponseDto);
        Optional<UserResponseDto> resultOptional = userService.findById(testUserId);

        //assert
        assertTrue(resultOptional.isPresent(),"Result optional should not be empty");
        assertEquals(sampleResponseDto,resultOptional.get(),"Result optional should match Dto");

        //verfiy
        verify(mockUserRepository, times(1)).findById(testUserId);
        verify(mockUserMapper, times(1)).toResponseDto(sampleUser);
        verifyNoMoreInteractions(mockUserRepository, mockUserMapper);
    }

    @Test
    void findById_whenUserDoesNotExist_shouldReturnEmptyOptional() {
        Long nonExistentId = 99L;
        when(mockUserRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<UserResponseDto> resultOptional = userService.findById(nonExistentId);

        // Assert: Check the result
        assertFalse(resultOptional.isPresent(), "Result Optional should be empty");
        // Or alternatively: assertTrue(resultOptional.isEmpty());

        // Verify: Ensure repository was called, but mapper was NOT
        verify(mockUserRepository, times(1)).findById(nonExistentId);
        // Verify that the mapper's toResponseDto method was never called
        verify(mockUserMapper, never()).toResponseDto(any(User.class));
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);
    }
}