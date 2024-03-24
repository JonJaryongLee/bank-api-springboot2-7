package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param username
     * @param password
     * @param nickname
     * @return 등록된 회원의 username 을 반환합니다.
     * @throws IllegalArgumentException username, password, nickname 이 비어있는 경우 예외를 발생시킵니다.
     * @throws IllegalStateException    이미 존재하는 username 일 경우 예외를 발생시킵니다.
     */
    public String signUp(String username, String password, String nickname) {
        validateEmptyFields(username, password, nickname);
        validateNewMember(username);
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setNickname(nickname);
        memberRepository.save(member);
        return username;
    }

    private void validateEmptyFields(String username, String password, String nickname) {
        String eMessage = "회원 가입 중 에러가 발생했습니다: ";
        String type = "";
        if (username == null || username.trim().isEmpty()) {
            type = "Username cannot be empty";
        } else if (password == null || password.trim().isEmpty()) {
            type = "Password cannot be empty";
        } else if (nickname == null || nickname.trim().isEmpty()) {
            type = "Nickname cannot be empty";
        } else {
            return;
        }
        throw new IllegalArgumentException(eMessage + type);
    }

    private void validateNewMember(String username) {
        memberRepository.findByUsername(username).ifPresent(u -> {
            throw new IllegalStateException("회원 가입 중 에러가 발생했습니다: Username already exists");
        });
    }
}