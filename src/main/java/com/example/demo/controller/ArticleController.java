package com.example.demo.controller;

import com.example.demo.controller.response.ArticleDto;
import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .map(a -> new ArticleDto(a.getId(), a.getMember().getUsername(), a.getTitle(), a.getContent(), a.getCreatedAt(), a.getUpdatedAt()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
