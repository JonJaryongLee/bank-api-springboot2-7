package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductWithOption {

    private Long id;
    private String finPrdtCd;
    private String dclsMonth;
    private String korCoNm;
    private String finPrdtNm;
    private String etcNote;
    private Product.JoinDeny joinDeny;
    private Product.JoinMember joinMember;
    private Product.JoinWay joinWay;
    private String spclCnd;
    private ProductOption.IntrRateType intrRateType;
    private Double intrRate;
    private Double intrRate2;
    private ProductOption.SaveTrm saveTrm;
}
