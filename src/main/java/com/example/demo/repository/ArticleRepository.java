package com.example.demo.repository;

import com.example.demo.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Override
    @EntityGraph(value = "article_with_user")
    List<Article> findAll();

    @Override
    @EntityGraph(value = "article_with_user")
    Optional<Article> findById(Long id);
}
