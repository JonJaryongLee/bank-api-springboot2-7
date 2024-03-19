package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    Optional<ProductOption> findByProduct(Product product);
}
