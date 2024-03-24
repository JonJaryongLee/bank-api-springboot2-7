package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

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
}