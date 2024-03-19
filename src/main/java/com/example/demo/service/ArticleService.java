package com.example.demo.service;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * 모든 게시글을 조회합니다.
     * @return 게시글 목록
     */
    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    /**
     * 모든 게시글을 조회하되, 작성자 정보를 포함합니다.
     * @return 게시글 목록
     */
    public List<Article> findArticlesWithUser() {
        return articleRepository.findAllWithUser();
    }

    /**
     * 주어진 번호에 해당하는 게시글을 조회합니다.
     * @param articleNo 조회할 게시글 번호
     * @return 조회된 게시글
     */
    public Article findArticle(Long articleNo) {
        Article article = verifyArticleExists(articleNo);
        return article;
    }

    /**
     * 주어진 번호에 해당하는 게시글을 조회하되, 작성자 정보를 포함합니다.
     * @param articleNo 조회할 게시글 번호
     * @return 조회된 게시글
     */
    public Article findArticleWithUser(Long articleNo) {
        verifyArticleExists(articleNo);
        return articleRepository.findByIdWithUser(articleNo)
                .orElse(new Article());
    }

    /**
     * 새로운 게시글을 생성합니다.
     * @param article 생성할 게시글
     */
    @Transactional
    public void createArticle(Article article) {
        verifyArticleNotNull(article);
        verifyEmptyFields(article);
        articleRepository.save(article);
    }

    /**
     * 주어진 번호에 해당하는 게시글을 삭제합니다.
     * @param articleNo 삭제할 게시글 번호
     */
    @Transactional
    public void deleteArticle(Long articleNo) {
        Article article = findArticle(articleNo);
        articleRepository.deleteById(articleNo);
    }

    /**
     * 게시글을 업데이트합니다.
     * @param article 업데이트할 게시글
     */
    @Transactional
    public void updateArticle(Article article) {
        verifyArticleNotNull(article);
        verifyEmptyFields(article);
        verifyArticleExists(article.getId());
        articleRepository.save(article);
    }

    /**
     * 주어진 게시글이 존재하는지 검증합니다.
     * @param articleNo 검증할 게시글
     * @throws IllegalStateException 만약 존재하지 않는 게시물이라면, IllegalStateException 을 발생시킵니다.
     */
    private Article verifyArticleExists(Long articleNo) {
        return articleRepository.findById(articleNo).orElseThrow(
                () -> new IllegalStateException("Article does not exist"));
    }

    /**
     * 주어진 게시글이 null이 아닌지 검증합니다.
     * @param article 검증할 게시글
     * @throws IllegalArgumentException 만약 전달한 article 이 null 이라면 IllegalArgumentException 을 발생시킵니다.
     */
    private void verifyArticleNotNull(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article is null");
        }
    }

    /**
     * Article 객체의 title 필드와 content 필드가 null이 아니거나 비어 있지 않음을 검증합니다.
     *
     * @param article 검증할 Article 객체
     * @throws IllegalArgumentException 만약 Article 객체의 title 필드나 content 필드가 null이거나 공백 문자만 포함하고 있다면 IllegalArgumentException을 발생시킵니다.
     */
    private void verifyEmptyFields(Article article) {
        if (article.getTitle() == null || article.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        } else if (article.getContent() == null || article.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }
}
