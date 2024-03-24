package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 게시글을 조회합니다.
     *
     * @return 게시글 목록
     */
    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    /**
     * 주어진 번호에 해당하는 게시글을 조회합니다.
     *
     * @param articleNo 조회할 게시글 번호
     * @return 조회된 게시글 객체
     * @throws NoSuchElementException 만약 존재하지 않는 게시물이라면, NoSuchElementException 을 발생시킵니다.
     */
    public Article findArticle(Long articleNo) {
        Article article = verifyArticleExists(articleNo);
        return article;
    }

    /**
     * 새로운 게시글을 생성합니다.
     *
     * @param username
     * @param title
     * @param content
     * @return 생성한 게시글의 아이디
     * @throws IllegalArgumentException username, title, content 가 비어있다면 IllegalArgumentException을 발생시킵니다.
     */
    @Transactional
    public Long createArticle(String username, String title, String content) {
        verifyEmptyFields(username, title, content);
        Member foundMember = validateMember(username);
        Article article = new Article();
        article.setMember(foundMember);
        article.setTitle(title);
        article.setContent(content);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    /**
     * 주어진 번호에 해당하는 게시글을 삭제합니다.
     *
     * @param articleNo 삭제할 게시글 번호
     * @return 삭제한 게시글의 아이디
     */
    @Transactional
    public Long deleteArticle(Long articleNo) {
        articleRepository.deleteById(articleNo);
        return articleNo;
    }

    /**
     * 게시글을 수정합니다.
     *
     * @param articleNo
     * @param username
     * @param title
     * @param content
     * @return 수정한 게시글의 아이디
     * @throws IllegalArgumentException articleNo, username, title, content 가 비어있다면 예외를 발생시킵니다.
     * @throws NoSuchElementException   articleNo, username 에 해당하는 사용자가 존재하지 않으면 예외를 발생시킵니다.
     */
    @Transactional
    public Long updateArticle(Long articleNo, String username, String title, String content) {
        verifyEmptyArticleNo(articleNo);
        verifyEmptyFields(username, title, content);
        verifyArticleExists(articleNo);
        Member foundMember = validateMember(username);
        Article article = new Article();
        article.setId(articleNo);
        article.setMember(foundMember);
        article.setTitle(title);
        article.setContent(content);
        articleRepository.findById(articleNo).ifPresent(a -> {
            article.setCreatedAt(a.getCreatedAt());
        });
        article.setUpdatedAt(LocalDateTime.now());
        Article updatedArticle = articleRepository.save(article);
        return updatedArticle.getId();
    }

    private void verifyEmptyArticleNo(Long articleNo) {
        if (articleNo == null) {
            throw new IllegalArgumentException("ArticleNo cannot be empty");
        }
    }

    private void verifyEmptyFields(String username, String title, String content) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        } else if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }

    private Article verifyArticleExists(Long articleNo) {
        return articleRepository.findById(articleNo).orElseThrow(
                () -> new NoSuchElementException("Article does not exist"));
    }

    private Member validateMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Member does not exist"));
    }
}
