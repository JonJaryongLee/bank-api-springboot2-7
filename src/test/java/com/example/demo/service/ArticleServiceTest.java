package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void findArticlesTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);

        articleService.createArticle("username", "title1", "content1");
        articleService.createArticle("username", "title2", "content2");

        // when
        List<Article> foundArticles = articleService.findArticles();

        // then
        Assertions.assertThat(foundArticles.size()).isEqualTo(2);
    }

    @Test
    public void findArticleTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);
        Long savedArticleId = articleService.createArticle("username", "title", "content");

        // when
        Article foundArticle = articleService.findArticle(savedArticleId);

        // then
        Assertions.assertThat(foundArticle.getId()).isEqualTo(savedArticleId);
        Assertions.assertThat(foundArticle.getMember().getUsername()).isEqualTo("username");
        Assertions.assertThat(foundArticle.getTitle()).isEqualTo("title");
        Assertions.assertThat(foundArticle.getContent()).isEqualTo("content");
//         Assertions.assertThat(foundArticle.getCreatedAt()).isEqualTo(LocalDateTime.now());
//         Assertions.assertThat(foundArticle.getUpdatedAt()).isEqualTo(LocalDateTime.now());
    }

    @Test
    public void deleteArticleTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);
        Long savedArticleId = articleService.createArticle("username", "title", "content");

        // when
        Long deletedId = articleService.deleteArticle(savedArticleId);
        Optional<Article> invalidId = articleRepository.findById(deletedId);

        // then
        Assertions.assertThat(deletedId).isEqualTo(savedArticleId);
        Assertions.assertThat(invalidId).isEqualTo(Optional.empty());
    }

    @Test
    public void updateArticleTest() {
        // given
        Member member = new Member();
        member.setUsername("username");
        member.setPassword("password");
        member.setNickname("nickname");
        memberRepository.save(member);
        Long savedArticleId = articleService.createArticle("username", "title", "content");

        // when
        Long updatedArticleId = articleService.updateArticle(savedArticleId, "username", "fixedTitle", "content");
        Article foundArticle = articleRepository.findById(updatedArticleId).get();

        // then
        Assertions.assertThat(foundArticle.getId()).isEqualTo(updatedArticleId);
        Assertions.assertThat(foundArticle.getMember().getUsername()).isEqualTo("username");
        Assertions.assertThat(foundArticle.getTitle()).isEqualTo("fixedTitle");
        Assertions.assertThat(foundArticle.getContent()).isEqualTo("content");
//        Assertions.assertThat(foundArticle.getCreatedAt()).isEqualTo(foundArticle.getUpdatedAt());
    }

    @Test
    public void validateMemberTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> articleService.createArticle("invaliduser", "title", "content"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Member does not exist");

    }

    @Test
    public void verifyArticleExistTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> articleService.findArticle(99L));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Article does not exist");
    }

    @Test
    public void verifyEmptyArticleNoTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> articleService.updateArticle(null, "testusername", "testtitle", "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("ArticleNo cannot be empty");
    }

    @Test
    public void verifyEmptyUsernameTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle("  ", "testtitle", "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Username cannot be empty");
    }

    @Test
    public void verifyEmptyTitleTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle("testusername", "    ", "testcontent"));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Title cannot be empty");
    }

    @Test
    public void verifyEmptyContentTest() {
        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> articleService.createArticle("testusername", "testtitle", "    "));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Content cannot be empty");
    }
}