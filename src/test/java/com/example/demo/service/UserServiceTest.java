package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setNickname("testNickname");
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteByUsername(user.getUsername());
    }

    @Test
    public void logInTest() {
        // given
        userService.signUp(user);

        // when
        String token = userService.logIn("testUser", "testPassword");

        // then
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void signUpFailTest() {
        // given
        userService.signUp(user);

        // when
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> userService.signUp(user));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User ID already exists");
    }
}