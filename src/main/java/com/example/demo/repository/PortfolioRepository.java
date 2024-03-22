package com.example.demo.repository;

import com.example.demo.entity.Member;
import com.example.demo.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByMember(Member member);
}
