package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.User;
import com.example.demo.repository.ArticleRepository;
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
class ArticleServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired ArticleRepository articleRepository;
    @Autowired ArticleService articleService;

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
    public void findArticlesWithUserTest() {
        // given
        List<Article> articles = articleService.findArticlesWithUser();

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
    }

    @Test
    public void findArticleWithUserTest() {
        // given
        Long articleNo = article.getId();

        // when
        Article foundArticle = articleService.findArticleWithUser(articleNo);

        // then
        Assertions.assertThat(foundArticle).isNotNull();
        Assertions.assertThat(foundArticle.getId()).isEqualTo(articleNo);
    }

    @Test
    public void createArticleTest() {
        // given
        Article newArticle = new Article();
        newArticle.setUser(user);
        newArticle.setTitle("new subject");
        newArticle.setContent("new content");

        // when
        articleService.createArticle(newArticle);

        // then
        Article createdArticle = articleService.findArticle(newArticle.getId());
        Assertions.assertThat(createdArticle).isNotNull();
        Assertions.assertThat(createdArticle.getId()).isEqualTo(newArticle.getId());
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

        // when
        articleService.updateArticle(article);

        // then
        Article updatedArticle = articleService.findArticle(article.getId());
        Assertions.assertThat(updatedArticle).isNotNull();
        Assertions.assertThat(updatedArticle.getContent()).isEqualTo(updatedContent);
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
}