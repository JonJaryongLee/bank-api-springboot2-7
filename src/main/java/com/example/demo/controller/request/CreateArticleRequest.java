package com.example.demo.controller.request;

import lombok.Data;

@Data
public class CreateArticleRequest {

    private String username;
    private String title;
    private String content;
}
