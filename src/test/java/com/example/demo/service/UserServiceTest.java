package com.example.demo.service;

import com.example.demo.entity.Portfolio;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

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
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void signUpTest() {
        // given
        String username = user.getUsername();

        // when
        String token = userService.signUp(user);

        // then
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(userRepository.findByUsername(username)).isNotNull();
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
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.signUp(user));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User ID already exists");
    }

    @Test
    public void logInNonExistentUserTest() {
        // given
        String nonExistentUserId = "nonExistentUser";
        String anyPassword = "anyPassword";

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.logIn(nonExistentUserId, anyPassword));

        // then
        Assertions.assertThat(e).isNotNull();
        Assertions.assertThat(e.getMessage()).isEqualTo("User does not exist");
    }

    @Test
    public void logInInvalidPasswordTest() {
        // given
        userService.signUp(user);

        String existentUserId = "testUser";
        String invalidPassword = "invalidPassword";

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.logIn(existentUserId, invalidPassword));

        // then
        Assertions.assertThat(e).isNotNull();
        Assertions.assertThat(e.getMessage()).isEqualTo("로그인 중 에러가 발생했습니다: Invalid password");
    }

    @Test
    public void signUpWithEmptyUsernameTest() {
        // given
        String emptyUsername = "    ";
        String validPassword = "validPassword";
        String validNickname = "Test User";

        User user = new User();
        user.setUsername(emptyUsername);
        user.setPassword(validPassword);
        user.setNickname(validNickname);

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(user));

        // then
        Assertions.assertThat(e).isNotNull();
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Username cannot be empty");
    }

    @Test
    public void signUpWithEmptyNicknameTest() {
        // given
        String validUsername = "sample";
        String validPassword = "validPassword";
        String emptyNickname = "     ";

        User user = new User();
        user.setUsername(validUsername);
        user.setPassword(validPassword);
        user.setNickname(emptyNickname);

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(user));

        // then
        Assertions.assertThat(e).isNotNull();
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Nickname cannot be empty");
    }

    @Test
    public void signUpWithEmptyPasswordTest() {
        // given
        String validUsername = "sample";
        String emptyPassword = "   ";
        String validNickname = "mynickname";

        User user = new User();
        user.setUsername(validUsername);
        user.setPassword(emptyPassword);
        user.setNickname(validNickname);

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(user));

        // then
        Assertions.assertThat(e).isNotNull();
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: User Password cannot be empty");
    }

    @Test
    public void signUpWithTendencyTest() {
        // given
        user.setTendency(User.Tendency.성실형);

        // when
        String token = userService.signUp(user);

        // then
        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(userRepository.findByUsername(user.getUsername())).isNotNull();
    }

    @Test
    public void findPortfoliosWithUser() {
        // given
        userRepository.save(user);

        Product product1 = new Product();
        Product product2 = new Product();
        productRepository.save(product1);
        productRepository.save(product2);

        Portfolio portfolio1 = new Portfolio();
        Portfolio portfolio2 = new Portfolio();
        portfolio1.setUser(user);
        portfolio1.setProduct(product1);
        portfolioRepository.save(portfolio1);
        portfolio2.setUser(user);
        portfolio2.setProduct(product2);
        portfolioRepository.save(portfolio2);

        // when
        User foundUser = userRepository.findByIdWithProducts(user.getId()).get();

        // then
        Assertions.assertThat(foundUser.getPortfolios()).isEqualTo("");

//        // when
//        List<Portfolio> portfolios = userService.findPortfoliosWithUser(user.getUsername());
//
//        // then
//        Assertions.assertThat(portfolios.size()).isEqualTo(2L);
    }

    @Test
    public void validateUserWithProductsTest() {
        // given

        // when

        // then
    }
}