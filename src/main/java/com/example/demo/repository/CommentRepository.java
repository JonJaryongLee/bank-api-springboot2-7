package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c")
    @EntityGraph(value = "comment_with_user_and_article")
    List<Comment> findAllWithUserAndArticle();

    @Query("SELECT c FROM Comment c WHERE c.id = ?1")
    @EntityGraph(value = "comment_with_user_and_article")
    Optional<Comment> findByIdWithUserAndArticle(Long id);
}
