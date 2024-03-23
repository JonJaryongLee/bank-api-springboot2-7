package com.example.demo.controller.input;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginDto {

    @NonNull
    private String username;
    @NonNull
    private String password;
}
