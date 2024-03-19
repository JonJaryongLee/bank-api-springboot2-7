package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_option")
@Getter @Setter
public class ProductOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "intr_rate_type_nm")
    private IntrRateType intrRateType;

    public enum IntrRateType {
        단리, 복리
    }

    @Column(name = "intr_rate")
    private Double intrRate;

    @Column(name = "intr_rate2")
    private Double intrRate2;

    @Enumerated(EnumType.STRING)
    @Column(name = "save_trm")
    private SaveTrm saveTrm;

    @Getter
    public enum SaveTrm {

        SIX("6"), TWELVE("12"), TWENTY_FOUR("24"), THIRTY_SIX("36");

        private String value;

        SaveTrm(String value) {
            this.value = value;
        }
    }
}
