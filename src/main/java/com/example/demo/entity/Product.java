package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "dcls_month")
    private String dclsMonth;

    @Column(name = "kor_co_nm")
    private String korCoNm;

    @Column(name = "fin_prdt_nm")
    private String finPrdtNm;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_deny")
    private JoinDeny joinDeny;

    public enum JoinDeny {
        제한없음, 서민전용, 일부제한
    }

    @Column(name = "spcl_cnd")
    private String spclCnd;
}
