package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user_with_products")
    @Query("SELECT u FROM User u")
    List<User> findAllWithProducts();

    @EntityGraph(value = "user_with_products")
    @Query("SELECT u FROM User u WHERE u.id = ?1")
    Optional<User> findByIdWithProducts(Long id);

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}