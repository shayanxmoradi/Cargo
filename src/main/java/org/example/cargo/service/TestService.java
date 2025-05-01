package org.example.cargo.service;

import org.example.cargo.dto.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final UserMapper userMapper;

    public TestService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
