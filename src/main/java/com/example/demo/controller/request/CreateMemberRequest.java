package com.example.demo.controller.request;

import lombok.Data;

@Data
public class CreateMemberRequest {

    private String username;
    private String password;
    private String nickname;
}
