package org.example.cargo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.dto.UserPatchDto;
import org.example.cargo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.*; // Import Hamcrest matchers
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Import MockMvcRequestBuilders
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Import MockMvcResultMatchers

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // For debugging output

@SpringBootTest // load full spring applicaiton context
@AutoConfigureMockMvc // configures mockmvc for sending http req without server
@ActiveProfiles("test")//uses tset.propteries
@Transactional// rolls db trancscatin after each test method
//@ExtendWith(SpringExtension.class)
public class UserResourceIT {
//DI

    @Autowired
    private MockMvc mockMvc;// for http reqs
    @Autowired
    private ObjectMapper objectMapper;// for convert pojo to json

    @Autowired
    private UserRepository userRepository;// direct Db access

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        testUser1 = new User();
        testUser1.setUsername("testUser1");
        testUser1.setPassword("password");
        testUser1.setFirstName("testUser1");
        testUser1.setLastName("testUser1");
        testUser1.setEmail("shayan@gmail.com");

        testUser1 = userRepository.save(testUser1);


        testUser2 = new User();
        testUser2.setUsername("testuser2");
        testUser2.setPassword("password");
        testUser2.setFirstName("Another");
        testUser2.setLastName("UserTwo");
        testUser2.setEmail("test2@example.com");
        testUser2 = userRepository.save(testUser2);
    }

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(userRepository);
    }

    @Test
    void createUser_whenVAlidInput_shouldReturnCreatedUser() throws Exception {
        //todo this id
        UserCreateDto createDto = new UserCreateDto(

                "shayans",
                "morads",
                "New@gmail.com",
                "absdgfu7",
                "usinglane"
        );

        // 2. Act & Assert: Perform POST request and verify response
        mockMvc.perform(post("/user") // Use post() method
                        .contentType(MediaType.APPLICATION_JSON) // Set request header
                        .content(objectMapper.writeValueAsString(createDto))) // Set request body as JSON
                .andDo(print()) // Print request/response details (useful for debugging)
                .andExpect(status().isCreated()) // Check for HTTP 201 Created status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Check response content type
                // Use jsonPath to check fields in the response body (UserResponseDto)
                .andExpect(jsonPath("$.id", is(notNullValue()))) // ID should be generated and not null
                .andExpect(jsonPath("$.username", is("usinglane")))
                .andExpect(jsonPath("$.firstName", is("shayans")))
                .andExpect(jsonPath("$.lastName", is("morads")))
                .andExpect(jsonPath("$.email", is("New@gmail.com")));

        // 3. Assert (Optional): Verify persistence in the database
        assertTrue(userRepository.findByUsername("usinglane").isPresent(),
                "User 'newuser' should exist in the database after creation.");
    }

    @Test
    void geUserById_whenUserExists_shouldReturnUser() throws Exception {

        mockMvc.perform(get("/user/{id}", testUser1.getId()) // Use the ID of the user created in setUp
                        .accept(MediaType.APPLICATION_JSON)) // Indicate we want JSON response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) testUser1.getId().longValue())))
                .andExpect(jsonPath("$.username", is(testUser1.getUsername())))
                .andExpect(jsonPath("$.firstName", is(testUser1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(testUser1.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser1.getEmail())));

    }

    @Test
    void getUserById_whenuserDoesNotExist_shouldReturnNotFound() throws Exception {
        int nonexistentId = 9999;
        mockMvc.perform(get("/user/{id}", nonexistentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllUsers_shouldReturnUserPage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/user")
                        .param("page", "0") // Request the first page
                        .param("size", "10") // Request page size of 10
                        // Optional: Add sort parameter for predictable order if needed
                        // .param("sort", "username,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Expect JSON response
                // Check page metadata
                .andExpect(jsonPath("$.totalElements", is(2))) // Should have 2 users in total
                .andExpect(jsonPath("$.totalPages", is(1))) // Should be 1 page total for size 10
                .andExpect(jsonPath("$.size", is(10))) // The page size should be 10
                .andExpect(jsonPath("$.number", is(0))) // The page number should be 0
                // Check the content array
                .andExpect(jsonPath("$.content", hasSize(2))) // The content array should have 2 users
                // Check details of the first user in the content (assuming default ID sort)
                .andExpect(jsonPath("$.content[0].id", is((int) testUser1.getId().longValue())))
                .andExpect(jsonPath("$.content[0].username", is(testUser1.getUsername())))
                // Check details of the second user
                .andExpect(jsonPath("$.content[1].id", is((int) testUser2.getId().longValue())))
                .andExpect(jsonPath("$.content[1].username", is(testUser2.getUsername())));
    }


    @Test
    void updateUser_whenUserExists_shouldReturnUpdatedUser() throws Exception {
        UserPatchDto changedFirstName = UserPatchDto.builder().firstName("xmm").build();
        mockMvc.perform(patch("/user/{id}", testUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changedFirstName)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) testUser1.getId().longValue())))
                .andExpect(jsonPath("$.username", is(testUser1.getUsername())))
                .andExpect(jsonPath("$.firstName", is(changedFirstName.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(testUser1.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser1.getEmail())));

        //Assert verfiy DB
        User patchedUserFromDb = userRepository.findById(testUser1.getId()).orElseThrow();
        assertEquals("xmm", patchedUserFromDb.getFirstName());
        assertEquals(testUser1.getLastName(), patchedUserFromDb.getLastName());
        assertEquals(testUser1.getEmail(), patchedUserFromDb.getEmail());
        assertEquals(testUser1.getUsername(), patchedUserFromDb.getUsername());
    }

    @Test
    void updateUser_whenUserDoesNotExist_shouldReturnNotFound() throws Exception {
        int nonexistentId = 9999;
        UserPatchDto changedFirstName = UserPatchDto.builder().firstName("xmm").build();

        mockMvc.perform(patch("/user/{id}", nonexistentId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(changedFirstName)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteUser_whenUserExists_shouldReturnNoContent() throws Exception {
        long userIdToDelete = testUser1.getId();
        long initialCount = userRepository.count(); // Should be 2

        mockMvc.perform(delete("/user/{id}", userIdToDelete))
                .andDo(print())
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

        // Assert (Optional): Verify user is actually deleted from the database
        assertFalse(userRepository.findById(userIdToDelete).isPresent(),
                "User with ID " + userIdToDelete + " should have been deleted.");
        assertEquals(initialCount - 1, userRepository.count(),
                "User count should decrease by 1 after deletion.");
    }
    @Test
    void deleteUser_whenUserDoesNotExist_shouldReturnNoContent() throws Exception {
        long nonExistentId = 9999L;
        long initialCount = userRepository.count(); // Should be 2

        mockMvc.perform(delete("/user/{id}", nonExistentId))
                .andDo(print())
                // Expect 204 No Content even if the user doesn't exist (idempotent)
                .andExpect(status().isNoContent());

        // Assert: Verify database state hasn't changed
        assertEquals(initialCount, userRepository.count(),
                "User count should remain unchanged when deleting a non-existent user.");
    }

}
