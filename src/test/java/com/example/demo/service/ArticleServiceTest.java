package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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


    }

    @Test
    public void validateMemberTest() {
    }

    @Test
    public void verifyArticleExistTest() {
    }

    @Test
    public void verifyEmptyArticleNoTest() {

    }

    @Test
    public void verifyEmptyTitleTest() {
    }

    @Test
    public void verifyEmptyContentTest() {
    }

    @Test
    public void findCommentsTest() {
    }
}