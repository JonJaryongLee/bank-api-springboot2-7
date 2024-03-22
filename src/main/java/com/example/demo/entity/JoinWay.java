package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "join_way")
public class JoinWay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_way_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private JoinWayName name;

    public enum JoinWayName {
        인터넷, 스마트폰, 텔레뱅킹, 영업점, 기타
    }
}