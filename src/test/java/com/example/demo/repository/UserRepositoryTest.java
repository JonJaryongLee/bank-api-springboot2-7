package com.example.demo.repository;

import com.example.demo.entity.User;
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
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

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
    void findByIdTest() {
        // given
        User savedUser = userRepository.save(user);

        // when
        User foundUser = userRepository.findById(savedUser.getId()).get();

        // then
        Assertions.assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    }
}