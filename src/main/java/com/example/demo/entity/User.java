package com.example.demo.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedEntityGraph(name = "user_with_products", attributeNodes = {
        @NamedAttributeNode(value = "portfolios", subgraph = "portfolios.product")
}, subgraphs = {
        @NamedSubgraph(name = "portfolios.product", attributeNodes = @NamedAttributeNode("product"))
})
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Portfolio> portfolios = new ArrayList<>();

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