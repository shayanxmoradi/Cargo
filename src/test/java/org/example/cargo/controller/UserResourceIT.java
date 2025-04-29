package org.example.cargo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.cargo.repository.UserRepository;
import org.example.cargo.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest // load full spring applicaiton context
@AutoConfigureMockMvc // configures mockmvc for sending http req without server
@ActiveProfiles("test")//uses tset.propteries
@Transactional// rolls db trancscatin after each test method
@ExtendWith(SpringExtension.class)
public class UserResourceIT {
//DI

    @Autowired
    private MockMvc mockMvc;// for http reqs
    @Autowired
    private ObjectMapper objectMapper;// for convert pojo to json

    @Autowired
    private UserRepository userRepository;// direct Db access

    @Test
    void contextLoads()  {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(userRepository);
    }


}
