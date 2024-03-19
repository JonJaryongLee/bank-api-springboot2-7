package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "fin_prdt_cd")
    private String finPrdtCd;

    @Column(name = "dcls_month")
    private String dclsMonth;

    @Column(name = "kor_co_nm")
    private String korCoNm;

    @Column(name = "pin_prdt_nm")
    private String finPrdtNm;

    @Column(name = "etc_note")
    private String etcNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_deny")
    private JoinDeny joinDeny;

    public enum JoinDeny {
        제한없음, 서민전용, 일부제한
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "join_member")
    private JoinMember joinMember;

    public enum JoinMember {
        실명의개인, 만50세이상, 제한없음
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "join_way")
    private JoinWay joinWay;

    public enum JoinWay {
        영업점, 인터넷, 스마트폰
    }

    @Column(name = "spcl_cnd")
    private String spclCnd;
}
