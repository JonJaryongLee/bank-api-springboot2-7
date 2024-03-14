package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @EntityGraph(value = "comment_with_user_and_article")
    List<Comment> findAll();

    @Override
    @EntityGraph(value = "comment_with_user_and_article")
    Optional<Comment> findById(Long id);
}
