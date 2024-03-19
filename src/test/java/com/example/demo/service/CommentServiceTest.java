package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired CommentService commentService;

    private User user;
    private Article article;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        // given
        user = new User();
        user.setUsername("user1");
        user.setPassword("samplePwd");
        user.setNickname("testnickname");
        userRepository.save(user);

        article = new Article();
        article.setUser(user);
        article.setTitle("test title");
        article.setContent("test content");
        articleRepository.save(article);

        comment = new Comment();
        comment.setUser(user);
        comment.setArticle(article);
        comment.setContent("test content");
        commentRepository.save(comment);
    }

    @AfterEach
    public void tearDown() {
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findCommentsTest() {
        // given
        List<Comment> comments = commentService.findComments();

        // then
        Assertions.assertThat(comments).isNotNull();
        Assertions.assertThat(comments.size()).isGreaterThan(0);
    }

    @Test
    public void findCommentTest() {
        // given

        // when
        Comment foundComment = commentService.findComment(comment.getId());

        // then
        Assertions.assertThat(foundComment.getId()).isEqualTo(comment.getId());
        Assertions.assertThat(foundComment.getContent()).isEqualTo(comment.getContent());
//        Assertions.assertThat(foundComment.getCreatedAt()).isEqualTo(LocalDateTime.now());
//        Assertions.assertThat(foundComment.getUpdatedAt()).isEqualTo(LocalDateTime.now());

        Assertions.assertThat(foundComment.getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThat(foundComment.getUser().getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(foundComment.getUser().getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(foundComment.getUser().getNickname()).isEqualTo(user.getNickname());

        Assertions.assertThat(foundComment.getArticle().getId()).isEqualTo(article.getId());
        Assertions.assertThat(foundComment.getArticle().getUser().getNickname()).isEqualTo(user.getNickname());
        Assertions.assertThat(foundComment.getArticle().getTitle()).isEqualTo(article.getTitle());
        Assertions.assertThat(foundComment.getArticle().getContent()).isEqualTo(article.getContent());
//        Assertions.assertThat(foundComment.getArticle().getCreatedAt()).isEqualTo(LocalDateTime.now());
//        Assertions.assertThat(foundComment.getArticle().getUpdatedAt()).isEqualTo(LocalDateTime.now());

    }

    @Test
    public void createCommentTest() {
        // given
        Comment newComment = new Comment();
        newComment.setUser(user);
        newComment.setArticle(article);
        newComment.setContent("new content");

        // when
        commentService.createComment(newComment);

        // then
        Comment createdComment = commentService.findComment(newComment.getId());
        Assertions.assertThat(createdComment).isNotNull();
        Assertions.assertThat(createdComment.getId()).isEqualTo(newComment.getId());
    }

    @Test
    public void deleteCommentTest() {
        // given
        Long commentNo = comment.getId();

        // when
        commentService.deleteComment(commentNo);

        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            commentService.findComment(commentNo);
        });
        String expectedMessage = "Comment does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void updateCommentTest() {
        // given
        String updatedContent = "updated content";
        comment.setContent(updatedContent);

        // when
        commentService.updateComment(comment);

        // then
        Comment updatedComment = commentService.findComment(comment.getId());
        Assertions.assertThat(updatedComment).isNotNull();
        Assertions.assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
//        Assertions.assertThat(updatedComment.getCreatedAt()).isEqualTo(LocalDateTime.now());
        Assertions.assertThat(updatedComment.getUpdatedAt()).isEqualTo(LocalDateTime.now());
    }

    @Test
    public void verifyCommentExistsExceptionTest() {
        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            commentService.findComment(99L);
        });

        // then
        String expectedMessage = "Comment does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void verifyCommentNotNullExceptionTest() {
        // given
        Comment nullComment = null;

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(nullComment);
        });

        // then
        String expectedMessage = "Comment is null";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void verifyEmptyContentExceptionTest() {
        // given
        Comment emptyFieldsComment = new Comment();
        emptyFieldsComment.setContent("     ");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(emptyFieldsComment);
        });

        // then
        String expectedMessage = "Content cannot be empty";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}

