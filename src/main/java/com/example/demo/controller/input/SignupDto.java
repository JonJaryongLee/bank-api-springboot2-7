package com.example.demo.controller.input;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignupDto {

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String nickname;

    private Long age;
    private Long money;
    private Long salary;
    private String favoriteBank;
    private String tendency;
}
