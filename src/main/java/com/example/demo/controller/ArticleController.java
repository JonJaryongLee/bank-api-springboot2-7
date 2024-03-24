package com.example.demo.controller;

import com.example.demo.controller.request.CreateArticleRequest;
import com.example.demo.controller.response.ArticleDto;
import com.example.demo.controller.response.CreateArticleResponse;
import com.example.demo.controller.response.DeleteArticleResponse;
import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public Result listArticle() {
        List<Article> foundArticles = articleService.findArticles();
        List<ArticleDto> collect = foundArticles.stream()
                .map(a -> new ArticleDto(
                        a.getId(),
                        a.getMember().getUsername(),
                        a.getTitle(),
                        a.getContent(),
                        a.getCreatedAt(),
                        a.getUpdatedAt()
                ))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @GetMapping("/articles/{id}")
    public ArticleDto detailArticle(@PathVariable("id") Long id) {
        Article article = articleService.findArticle(id);
        ArticleDto articleDto = new ArticleDto(
                article.getId(),
                article.getMember().getUsername(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
        return articleDto;
    }

    @PostMapping("/articles")
    public CreateArticleResponse create(@RequestBody CreateArticleRequest request) {
        Long savedArticleId = articleService.createArticle(
                request.getUsername(),
                request.getTitle(),
                request.getContent()
        );
        return new CreateArticleResponse(savedArticleId);
    }

    @DeleteMapping("/articles/{id}")
    public DeleteArticleResponse delete(@PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return new DeleteArticleResponse(id);
    }

}
