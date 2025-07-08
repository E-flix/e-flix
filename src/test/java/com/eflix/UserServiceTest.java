package com.eflix;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eflix.main.dto.UserDTO;
import com.eflix.main.mapper.UserMapper;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordCheck() {
        UserDTO userDTO = userMapper.findByUserId("aa");
        assertTrue(passwordEncoder.matches("aa", userDTO.getUserPw()));
    }

}
