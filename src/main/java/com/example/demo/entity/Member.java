package com.example.demo.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;

    @Column(name = "is_superuser", columnDefinition = "boolean default false")
    private Boolean isSuperuser = false;
    private String nickname;
    private Long age;
    private Long money;
    private Long salary;

    @Column(name = "favorite_bank")
    private String favoriteBank;

    @Enumerated(EnumType.STRING)
    private Tendency tendency;

    public enum Tendency {
        알뜰형, 도전형, 성실형
    }
}