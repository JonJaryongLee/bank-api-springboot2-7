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
import java.util.NoSuchElementException;
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
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
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
    public void findPortfoliosTest() {
        // given
        userRepository.save(user);

        Product product1 = new Product();
        Product product2 = new Product();

        product1.setFinPrdtCd("testFinPrdtCd1");
        product1.setDclsMonth("testDclsMonth1");
        product1.setKorCoNm("testKorCoNm1");
        product1.setFinPrdtNm("testFinPrdtNm1");
        product1.setEtcNote("testEtcNote1");
        product1.setJoinDeny(Product.JoinDeny.서민전용);
        product1.setJoinMember(Product.JoinMember.만50세이상);
        product1.setJoinWay(Product.JoinWay.스마트폰);

        product2.setFinPrdtCd("testFinPrdtCd2");
        product2.setDclsMonth("testDclsMonth2");
        product2.setKorCoNm("testKorCoNm2");
        product2.setFinPrdtNm("testFinPrdtNm2");
        product2.setEtcNote("testEtcNote2");
        product2.setJoinDeny(Product.JoinDeny.서민전용);
        product2.setJoinMember(Product.JoinMember.만50세이상);
        product2.setJoinWay(Product.JoinWay.스마트폰);

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
        List<Portfolio> portfolios = userService.findPortfolios(user.getUsername());

        // then
        Assertions.assertThat(portfolios.size()).isEqualTo(2L);
        Assertions.assertThat(portfolios.get(0).getProduct().getFinPrdtCd()).isEqualTo("testFinPrdtCd1");
        Assertions.assertThat(portfolios.get(1).getProduct().getFinPrdtCd()).isEqualTo("testFinPrdtCd2");
    }

    @Test
    public void validateUserTest() {
        // given

        // when
        Optional<User> nonExistUser = userRepository.findByUsername("NonExistUser");

        // then
        Assertions.assertThat(nonExistUser).isEqualTo(Optional.empty());
    }
}