package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        user.setUsername("testname");
        user.setNickname("testnickname");
        userRepository.save(user);

        article = new Article();
        article.setUser(user);
        article.setTitle("test subject");
        article.setContent("test content");
        articleRepository.save(article);

        comment = new Comment();
        comment.setUser(user);
        comment.setContent("test content");
        commentRepository.save(comment);
    }

    @AfterEach
    public void tearDown() {
        List<Article> articles = articleRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            if (comment.getUser().getId().equals(user.getId())) {
                commentRepository.deleteById(comment.getId());
            }
        }
        for (Article article : articles) {
            if (article.getUser().getId().equals(user.getId())) {
                articleRepository.deleteById(article.getId());
            }
        }
        userRepository.deleteById(user.getId());
    }

    @Test
    public void testFindComments() {
        // given
        List<Comment> comments = commentService.findComments();

        // then
        Assertions.assertThat(comments).isNotNull();
        Assertions.assertThat(comments.size()).isGreaterThan(0);
    }

    @Test
    public void testFindComment() {
        // given
        Long commentNo = comment.getId();

        // when
        Comment foundComment = commentService.findComment(commentNo);

        // then
        Assertions.assertThat(foundComment).isNotNull();
        Assertions.assertThat(foundComment.getId()).isEqualTo(commentNo);
    }

    @Test
    public void testCreateComment() {
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
    public void testDeleteComment() {
        // given
        Long commentNo = comment.getId();

        // when
        commentService.deleteComment(commentNo);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.findComment(commentNo);
        });
        String expectedMessage = "Comment does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testUpdateComment() {
        // given
        String updatedContent = "updated content";
        comment.setContent(updatedContent);

        // when
        commentService.updateComment(comment);

        // then
        Comment updatedComment = commentService.findComment(comment.getId());
        Assertions.assertThat(updatedComment).isNotNull();
        Assertions.assertThat(updatedComment.getContent()).isEqualTo(updatedContent);
    }

    @Test
    public void testVerifyCommentExistsException() {
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.findComment(99L);
        });

        // then
        String expectedMessage = "Comment does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testVerifyCommentNotNullException() {
        // given
        Comment nullComment = null;

        // when
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            commentService.createComment(nullComment);
        });

        // then
        String expectedMessage = "Comment is null";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void testVerifyEmptyContentException() {
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

