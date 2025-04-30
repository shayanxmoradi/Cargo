package org.example.cargo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.cargo.domain.user.User;
import org.example.cargo.dto.UserCreateDto;
import org.example.cargo.repository.UserRepository;
import org.example.cargo.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.*; // Import Hamcrest matchers
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue; // Import assertTrue
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Import MockMvcRequestBuilders
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // Import MockMvcResultMatchers
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
    void contextLoads()  {
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

}
