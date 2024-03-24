package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.JoinWay;

import java.util.List;

public interface JoinWayRepository extends JpaRepository<JoinWay, Long> {

    List<JoinWay> findByProduct(Product product);
}
