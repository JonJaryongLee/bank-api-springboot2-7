package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.Product;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PortfolioRepository portfolioRepository;

    @Test
    public void signUpTest() {
        // given
        String username = "testusername";
        String password = "testpassword";
        String nickname = "testnickname";

        // when
        String savedUsername = memberService.signUp(username, password, nickname);
        Member foundMember = memberRepository.findByUsername(savedUsername).get();

        // then
        Assertions.assertThat(foundMember.getUsername()).isEqualTo(username);
        Assertions.assertThat(foundMember.getPassword()).isEqualTo(password);
        Assertions.assertThat(foundMember.getNickname()).isEqualTo(nickname);
    }

    @Test
    public void signUpFailTest() {
        // given
        String username = "testusername";
        String password = "testpassword";
        String nickname = "testnickname";
        memberService.signUp(username, password, nickname);

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.signUp(username, password, nickname));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Username already exists");
    }

    @Test
    public void signUpWithEmptyUsernameTest() {
        // given
        String emptyUsername = "    ";
        String validPassword = "validPassword";
        String validNickname = "Test User";

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> memberService.signUp(emptyUsername, validPassword, validNickname));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Username cannot be empty");
    }

    @Test
    public void signUpWithEmptyNicknameTest() {
        // given
        String validUsername = "sample";
        String validPassword = "validPassword";
        String emptyNickname = "     ";

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> memberService.signUp(validUsername, validPassword, emptyNickname));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Nickname cannot be empty");
    }

    @Test
    public void signUpWithEmptyPasswordTest() {
        // given
        String validUsername = "sample";
        String emptyPassword = "   ";
        String validNickname = "mynickname";

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> memberService.signUp(validUsername, emptyPassword, validNickname));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("회원 가입 중 에러가 발생했습니다: Password cannot be empty");
    }

    @Test
    public void findPortfoliosTest() {
        // given
        Member member = new Member();
        member.setUsername("testusername");
        member.setPassword("testpassword");
        member.setNickname("testnickname");
        memberRepository.save(member);

        Product product1 = new Product();
        Product product2 = new Product();
        product1.setDclsMonth("testmonth1");
        product2.setDclsMonth("testmonth2");
        product1.setKorCoNm("testkorconm1");
        product2.setKorCoNm("testkorconm2");
        product1.setFinPrdtNm("testkorconm1");
        product2.setFinPrdtNm("testkorconm2");
        product1.setJoinDeny(Product.JoinDeny.서민전용);
        product2.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product1);
        productRepository.save(product2);

        Portfolio portfolio1 = new Portfolio();
        Portfolio portfolio2 = new Portfolio();
        portfolio1.setProduct(product1);
        portfolio2.setProduct(product2);
        portfolio1.setMember(member);
        portfolio2.setMember(member);

        portfolioRepository.save(portfolio1);
        portfolioRepository.save(portfolio2);

        // when
        List<Portfolio> foundPortfolios = memberService.findPortfolios(member.getUsername());

        // then
        Assertions.assertThat(foundPortfolios.size()).isEqualTo(2);
    }

    @Test
    public void findPortfoliosFailedTest() {
        // given

        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> memberService.findPortfolios("invalidusername"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Member does not exist");
    }
}