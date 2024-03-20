package com.example.demo.controller.input;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class ChangePasswordDto {

    @NonNull
    private String newPassword;
}
