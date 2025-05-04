package org.example.cargo.service;

import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserMapper;
import org.example.cargo.dto.UserResponseDto;
import org.example.cargo.dto.UserUpdateDto;
import org.example.cargo.exception.ResourceNotFoundException;
import org.example.cargo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private final Long nonExistentId=999l;

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
    void setup_shouldInjectMocks() {
        assertNotNull(userService, "UserServiceImpl instance should be created");
        assertNotNull(mockUserMapper, "UserMapper instance should be created");
        assertNotNull(mockUserRepository, "UserRepository instance should be created");

    }

    @Test
    void findById_whenUserExists_shouldReturnUserDtoOptional() {
        when(mockUserRepository.findById(testUserId)).thenReturn(Optional.of(sampleUser));
        when(mockUserMapper.toResponseDto(sampleUser)).thenReturn(sampleResponseDto);
        Optional<UserResponseDto> resultOptional = userService.findById(testUserId);

        //assert
        assertTrue(resultOptional.isPresent(), "Result optional should not be empty");
        assertEquals(sampleResponseDto, resultOptional.get(), "Result optional should match Dto");

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


    @Test
    void save_shouldReturnSavedUSerDto() {
        UserCreateDto createDto = new UserCreateDto("shayan",
                "moradi",
                "sh.gmail.com",
                "123456f?",
                "shxxm");
        User userToSave = new User();
        userToSave.setUsername(createDto.username());
        userToSave.setPassword(createDto.password());
        userToSave.setFirstName(createDto.firstName());
        userToSave.setLastName(createDto.lastName());
        userToSave.setEmail(createDto.email());


        // Entity object returned by the repository save call (after saving, with ID)
        User savedUser = new User();
        savedUser.setId(2L); // Simulate ID generation
        savedUser.setUsername(createDto.username());
        savedUser.setPassword(createDto.password());
        savedUser.setFirstName(createDto.firstName());
        savedUser.setLastName(createDto.lastName());
        savedUser.setEmail(createDto.email());

        UserResponseDto expectedResponseDto = new UserResponseDto(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getUsername()
        );


        when(mockUserMapper.fromCreateDto(any(UserCreateDto.class))).thenReturn(userToSave);

        when(mockUserRepository.save(any(User.class))).thenReturn(savedUser);

        when(mockUserMapper.toResponseDto(any(User.class))).thenReturn(expectedResponseDto);

        UserResponseDto actualResponseDto = userService.save(createDto);

        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto, actualResponseDto);

        //verfiy
        ArgumentCaptor<User> userEntityCaptor = ArgumentCaptor.forClass(User.class);

        verify(mockUserMapper, times(1)).fromCreateDto(createDto);
        verify(mockUserRepository, times(1)).save(userEntityCaptor.capture());
        assertEquals(userToSave, userEntityCaptor.getValue(), "Entity passed to save should match entity from first mapping"); // Check captured value
        verify(mockUserMapper, times(1)).toResponseDto(savedUser); // Called with the saved entity
        verifyNoMoreInteractions(mockUserRepository, mockUserMapper);
    }
    // Arrange : prepare evreything needed. inputs dtos . expected things.
    // definde mock behaivier : wehen . donothing.
    //act: call method on @Injectedmocks
    //assert : verfiy method undertest produced the corerct result being excecuted in act. : assertEqual,asserttrue,assertnotnull
    //verify : optional : verifys, argumentcaptor,


    @Test
    void update_whenUserDoesNotExist_shouldThrowResourceNotFoundException() {
        Long nonExistenId = 999l;
        UserUpdateDto updateDto = new UserUpdateDto("upis", "nopis", "as@gmail.com", "sfs");
        when(mockUserRepository.findById(nonExistenId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.update(nonExistenId, updateDto), "shouold throw exception");

        verify(mockUserRepository, times(1)).findById(nonExistenId);
        verify(mockUserMapper,never()).toResponseDto(any(User.class));
        verify(mockUserRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);

    }

    @Test
    void update_whenUserExists_shouldReturnUpdatedUserDto() {
        // Arrange: Input DTO
        UserUpdateDto updateDto = new UserUpdateDto(
                testUserId.toString(),
                "UpdatedFirst",
                "UpdatedLast",
                "updated@example.com"
        );

        // Simulate the entity returned by findById (using sampleUser from setup)
        User existingUser = sampleUser; // This is the object the mapper will modify (conceptually)

        // Simulate the state of the entity *after* modification and saving
        // This represents the object returned by repository.save()
        User savedUpdatedUser = new User();
        savedUpdatedUser.setId(existingUser.getId());
        savedUpdatedUser.setUsername(existingUser.getUsername()); // Assume unchanged
        savedUpdatedUser.setPassword(existingUser.getPassword()); // Assume unchanged
        savedUpdatedUser.setFirstName(updateDto.firstName()); // Updated
        savedUpdatedUser.setLastName(updateDto.lastName());   // Updated
        savedUpdatedUser.setEmail(updateDto.email());       // Updated

        // Expected final DTO result
        UserResponseDto expectedResponseDto = new UserResponseDto(
                savedUpdatedUser.getId(),
                savedUpdatedUser.getFirstName(),
                savedUpdatedUser.getLastName(),
                savedUpdatedUser.getEmail(),
                savedUpdatedUser.getUsername()
        );

        // Define mock behavior
        // 1. Mock findById to return the existing user
        when(mockUserRepository.findById(testUserId)).thenReturn(Optional.of(existingUser));

        // 2. Mock the repository save call.
        //    It will be called with the 'existingUser' object because the void mapper
        //    method on the mock doesn't actually modify it.
        //    We mock save to return 'savedUpdatedUser' which has the updated state.
        when(mockUserRepository.save(any(User.class))).thenReturn(savedUpdatedUser);

        // 3. Mock the final mapping to DTO, based on the object returned by save
        when(mockUserMapper.toResponseDto(eq(savedUpdatedUser))).thenReturn(expectedResponseDto);

        // Act: Call the service method
        UserResponseDto actualResponseDto = userService.update(testUserId, updateDto);

        // Assert: Check the final result
        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto, actualResponseDto);

        // Verify: Check interactions
        // 1. Verify findById was called
        verify(mockUserRepository, times(1)).findById(testUserId);
        // 2. Verify the void mapper method was called with the correct DTO and the existing entity
        verify(mockUserMapper, times(1)).updateUserFromDto(eq(updateDto), eq(existingUser));
        // 3. Verify save was called, capturing the argument.
        //    Capture the argument to ensure save was called, but don't assert its fields for updates here.
        ArgumentCaptor<User> userCaptorForSave = ArgumentCaptor.forClass(User.class);
        verify(mockUserRepository, times(1)).save(userCaptorForSave.capture());
        //    Optionally, assert it's the same instance passed to the mapper if that's important.
        assertSame(existingUser, userCaptorForSave.getValue(), "Save method should be called with the object instance found by findById");
        // 4. Verify the final mapping to DTO was called with the result of save
        verify(mockUserMapper, times(1)).toResponseDto(eq(savedUpdatedUser));
        verifyNoMoreInteractions(mockUserRepository, mockUserMapper);
    }

//    @Test
//    void delete_whenUserDoesNotExist_shouldReturnEmptyOptional() {
//        Long nonExistenId = 999l;
//        when(mockUserRepository.findById(nonExistenId)).thenReturn(Optional.empty());
//        userService.deleteById(nonExistenId);
//    verify(mockUserRepository, times(1)).findById(nonExistenId);
//  //  verify(mockUserRepository, times(1)).findById(nonExistenId);
//    verifyNoMoreInteractions(mockUserRepository);
//    verifyNoInteractions(mockUserMapper);
//    }

    @Test
    void existsById_whenUserDoesNotExist_shouldReturnFalse() {
        // Arrange
        Long nonExistentId = 99L;
        when(mockUserRepository.existsById(nonExistentId)).thenReturn(false);

        // Act
        boolean result = userService.existsById(nonExistentId);

        // Assert
        assertFalse(result);

        // Verify
        verify(mockUserRepository, times(1)).existsById(nonExistentId);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);
    }

    @Test
    void delete_shouldCallRepositoryDelte(){
        //arange
        Long deletedUserId = sampleUser.getId();
      //  when(mockUserRepository.findById(deletedUserId)).thenReturn(Optional.ofNullable(any(User.class)));
        doNothing().when(mockUserRepository).deleteById(deletedUserId);

        //act
        userService.deleteById(deletedUserId);
        //verify
        //verify(mockUserRepository,times(1)).findById(deletedUserId);
        verify(mockUserRepository,times(1)).deleteById(deletedUserId);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);
    }

    @Test
    void existById_whenUserExists_shouldReturnTrue() {
        Long existenId = sampleUser.getId();

        when(mockUserRepository.existsById(existenId)).thenReturn(true);

        boolean result = userService.existsById(existenId);
        assertTrue(result,"should return true when user exists");

        verify(mockUserRepository, times(1)).existsById(existenId);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);
    }
    @Test
    void existById_whenUserDoesNotExists_shouldReturnFalse() {

        when(mockUserRepository.existsById(nonExistentId)).thenReturn(false);

        boolean result = userService.existsById(nonExistentId);
        assertFalse(result,"should return false when user does not exists");

        verify(mockUserRepository, times(1)).existsById(nonExistentId);
        verifyNoMoreInteractions(mockUserRepository);
        verifyNoInteractions(mockUserMapper);
    }

@Test
    void findByUsername_whenUserExists_shouldReturnUserDtoOptional() {
        //Arrange
        String existingUsername= sampleUser.getUsername();
        when(mockUserRepository.findByUsername(existingUsername)).thenReturn(Optional.of(sampleUser));
        when(mockUserMapper.toResponseDto(sampleUser)).thenReturn(sampleResponseDto);
//Act
    Optional<UserResponseDto> resualtOptional = userService.findByUsername(existingUsername);
    //assert
    assertTrue(resualtOptional.isPresent(),"shoult be present when username exists");
    assertEquals(resualtOptional.get(),sampleResponseDto,"should return the correct user");
    //verify
    verify(mockUserRepository, times(1)).findByUsername(existingUsername);
    verify(mockUserMapper,times(1)).toResponseDto(sampleUser);
    verifyNoMoreInteractions(mockUserRepository,mockUserMapper);//care to use it together

}

@Test
    void findByUsername_whenUserDoesNotExists_shouldReturnEmptyOptional() {
        String nonExistingUsername= "Asghar polo";
        when(mockUserRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        //Act
        Optional<UserResponseDto> resualtOptional = userService.findByUsername(nonExistingUsername);

        //Assert
    assertFalse(resualtOptional.isPresent(),"should return false when user does not exists");

    //verify
    verify(mockUserRepository, times(1)).findByUsername(nonExistingUsername);
    verifyNoMoreInteractions(mockUserRepository);
    verifyNoMoreInteractions(mockUserMapper);
}


    // Arrange : prepare evreything needed. inputs dtos . expected things.
    // definde mock behaivier : wehen . donothing.
    //act: call method on @Injectedmocks
    //assert : verfiy method undertest produced the corerct result being excecuted in act. : assertEqual,asserttrue,assertnotnull
    //verify : optional : verifys, argumentcaptor,

}