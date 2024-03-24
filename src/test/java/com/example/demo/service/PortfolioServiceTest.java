package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.entity.Portfolio;
import com.example.demo.entity.Product;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.ProductRepository;
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
class PortfolioServiceTest {

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PortfolioRepository portfolioRepository;

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
        List<Portfolio> foundPortfolios = portfolioService.findPortfolios(member.getUsername());

        // then
        Assertions.assertThat(foundPortfolios.size()).isEqualTo(2);
    }

    @Test
    public void findPortfoliosFailedTest() {
        // given

        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> portfolioService.findPortfolios("invalidusername"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Member does not exist");
    }
}