package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p")
    @EntityGraph(value = "product_with_option")
    List<Product> findAllWithOption();

    @Query("SELECT p FROM Product p WHERE p.id = ?1")
    @EntityGraph(value = "product_with_option")
    Optional<Product> findByIdWithOption(Long id);
}
