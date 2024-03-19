package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a")
    @EntityGraph(value = "article_with_user")
    List<Article> findAllWithUser();

    @Query("SELECT a FROM Article a WHERE a.id = ?1")
    @EntityGraph(value = "article_with_user")
    Optional<Article> findByIdWithUser(Long id);
}
