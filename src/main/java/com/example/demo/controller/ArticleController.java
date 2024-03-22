// package com.example.demo.controller;

// import com.example.demo.controller.output.ArticleListDto;
// import com.example.demo.entity.Article;
// import com.example.demo.service.ArticleService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.ArrayList;
// import java.util.List;

// @RestController
// @RequestMapping("/articles")
// @RequiredArgsConstructor
// public class ArticleController {

//     private final ArticleService articleService;

//     @GetMapping
//     public ResponseEntity<List<ArticleListDto>> getAllArticles() {
//         List<Article> articles = articleService.findArticles();
//         List<ArticleListDto> dtos = new ArrayList<>();
//         for (Article article : articles) {
//             ArticleListDto dto = new ArticleListDto();
//             dto.setId(article.getId());
//             dto.setUsername(article.getUser().getUsername());
//             dto.setTitle(article.getTitle());
//             dto.setContent(article.getContent());
//             dto.setCreatedAt(article.getCreatedAt());
//             dto.setUpdatedAt(article.getUpdatedAt());
//             dtos.add(dto);
//         }
//         if (dtos.isEmpty()) {
//             return new ResponseEntity<>(dtos, HttpStatus.NO_CONTENT);
//         } else {
//             return new ResponseEntity<>(dtos, HttpStatus.OK);
//         }
//     }
// }
