package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Rate;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    List<Rate> findByProduct(Product product);
}
