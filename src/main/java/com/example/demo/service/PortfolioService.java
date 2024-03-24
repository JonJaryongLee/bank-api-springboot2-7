package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.entity.Portfolio;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;

    /**
     * 주어진 사용자 이름과 관련된 포트폴리오를 찾습니다.
     *
     * @param username
     * @return 사용자와 관련된 포트폴리오 목록
     * @throws NoSuchElementException username 이 존재하지 않으면 예외를 발생합니다.
     */
    public List<Portfolio> findPortfolios(String username) {
        Member member = validateMember(username);
        return portfolioRepository.findByMember(member);
    }

    private Member validateMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Member does not exist"));
    }
}
