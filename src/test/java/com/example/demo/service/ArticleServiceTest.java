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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired ArticleService articleService;
    @Autowired CommentRepository commentRepository;

    private User user;
    private Article article;

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
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    @AfterEach
    public void tearDown() {
        articleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findArticlesTest() {
        // given
        List<Article> articles = articleService.findArticles();

        // then
        Assertions.assertThat(articles).isNotNull();
        Assertions.assertThat(articles.size()).isGreaterThan(0);
    }

    @Test
    public void findArticleTest() {
        // given
        Long articleNo = article.getId();

        // when
        Article foundArticle = articleService.findArticle(articleNo);

        // then
        Assertions.assertThat(foundArticle).isNotNull();
        Assertions.assertThat(foundArticle.getId()).isEqualTo(articleNo);
        Assertions.assertThat(foundArticle.getUser().getUsername()).isEqualTo("testname");
//        Assertions.assertThat(foundArticle.getCreatedAt()).isEqualTo(LocalDateTime.now());
//        Assertions.assertThat(foundArticle.getUpdatedAt()).isEqualTo(LocalDateTime.now());
    }

    @Test
    public void createArticleTest() {
        // given
        Article newArticle = new Article();
        newArticle.setUser(user);
        newArticle.setTitle("new subject");
        newArticle.setContent("new content");
        newArticle.setCreatedAt(LocalDateTime.now());
        newArticle.setUpdatedAt(LocalDateTime.now());

        // when
        articleService.createArticle(newArticle);

        // then
        Article createdArticle = articleService.findArticle(newArticle.getId());
        Assertions.assertThat(createdArticle).isNotNull();
        Assertions.assertThat(createdArticle.getId()).isEqualTo(newArticle.getId());
        Assertions.assertThat(createdArticle.getCreatedAt()).isEqualTo(newArticle.getCreatedAt());
        Assertions.assertThat(createdArticle.getUpdatedAt()).isEqualTo(newArticle.getUpdatedAt());
    }

    @Test
    public void deleteArticleTest() {
        // given
        Long articleNo = article.getId();

        // when
        articleService.deleteArticle(articleNo);

        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            articleService.findArticle(articleNo);
        });
        String expectedMessage = "Article does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void updateArticleTest() {
        // given
        String updatedContent = "updated content";
        article.setContent(updatedContent);
        article.setUpdatedAt(LocalDateTime.now());

        // when
        articleService.updateArticle(article);

        // then
        Article updatedArticle = articleService.findArticle(article.getId());
        Assertions.assertThat(updatedArticle).isNotNull();
        Assertions.assertThat(updatedArticle.getContent()).isEqualTo(updatedContent);
        Assertions.assertThat(updatedArticle.getCreatedAt()).isEqualTo(article.getCreatedAt());
        Assertions.assertThat(updatedArticle.getUpdatedAt()).isEqualTo(article.getUpdatedAt());
    }

    @Test
    public void verifyArticleExistsExceptionTest() {
        // when
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            articleService.findArticle(99L);
        });

        // then
        String expectedMessage = "Article does not exist";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void verifyArticleNotNullExceptionTest() {
        // given
        Article nullArticle = null;

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.createArticle(nullArticle);
        });

        // then
        String expectedMessage = "Article is null";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void verifyEmptyTitleExceptionTest() {
        // given
        Article emptyFieldsArticle = new Article();
        emptyFieldsArticle.setTitle("   ");
        emptyFieldsArticle.setContent("dasdfa");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.createArticle(emptyFieldsArticle);
        });

        // then
        String expectedMessage = "Title cannot be empty";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void verifyEmptyContentExceptionTest() {
        // given
        Article emptyFieldsArticle = new Article();
        emptyFieldsArticle.setTitle("dfasdf");
        emptyFieldsArticle.setContent("   ");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.createArticle(emptyFieldsArticle);
        });

        // then
        String expectedMessage = "Content cannot be empty";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void findCommentsTest() {
        // given
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        comment1.setUser(user);
        comment1.setArticle(article);
        comment1.setContent("test comment1");
        comment2.setUser(user);
        comment2.setArticle(article);
        comment2.setContent("test comment2");

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        List<Comment> comments = articleService.findComments(article.getId());

        // then
        Assertions.assertThat(comments.size()).isEqualTo(2L);
        Assertions.assertThat(comments.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(comments.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(comments.get(0).getArticle().getTitle()).isEqualTo("test subject");
        Assertions.assertThat(comments.get(1).getArticle().getTitle()).isEqualTo("test subject");
        Assertions.assertThat(comments.get(0).getUser().getNickname()).isEqualTo("testnickname");
        Assertions.assertThat(comments.get(1).getUser().getNickname()).isEqualTo("testnickname");
        Assertions.assertThat(comments.get(0).getContent()).isEqualTo("test comment1");
        Assertions.assertThat(comments.get(1).getContent()).isEqualTo("test comment2");
    }
}