package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setFinPrdtCd("testFinPrdtCd");
        product.setDclsMonth("testDclsMonth");
        product.setKorCoNm("testKorCoNm");
        product.setFinPrdtNm("testFinPrdtNm");
        product.setEtcNote("testEtcNote");
        product.setJoinDeny(Product.JoinDeny.서민전용);
        product.setJoinMember(Product.JoinMember.만50세이상);
        product.setJoinWay(Product.JoinWay.스마트폰);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }

}