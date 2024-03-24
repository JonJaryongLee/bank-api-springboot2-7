package com.example.demo.controller;

import com.example.demo.controller.request.CreateMemberRequest;
import com.example.demo.controller.response.CreateMemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public CreateMemberResponse signUpMember(@RequestBody CreateMemberRequest request) {
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setPassword(request.getPassword());
        member.setNickname(request.getNickname());
        memberService.signUp(member.getUsername(), member.getPassword(), member.getNickname());
        return new CreateMemberResponse(member.getUsername());
    }
}
