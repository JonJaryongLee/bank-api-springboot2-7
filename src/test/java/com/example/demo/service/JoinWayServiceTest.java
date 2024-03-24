package com.example.demo.service;

import com.example.demo.entity.JoinWay;
import com.example.demo.entity.Product;
import com.example.demo.repository.JoinWayRepository;
import com.example.demo.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JoinWayServiceTest {

    @Autowired
    JoinWayService joinWayService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JoinWayRepository joinWayRepository;

    @Test
    public void findJoinWaysTest() {
        // given
        Product product = new Product();
        product.setFinPrdtNm("testprdtnm1");
        product.setDclsMonth("testdclsmonth");
        product.setKorCoNm("testcorconm");
        product.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product);

        JoinWay joinWay1 = new JoinWay();
        joinWay1.setProduct(product);
        joinWay1.setName(JoinWay.JoinWayName.스마트폰);
        joinWayRepository.save(joinWay1);

        JoinWay joinWay2 = new JoinWay();
        joinWay2.setProduct(product);
        joinWay2.setName(JoinWay.JoinWayName.기타);
        joinWayRepository.save(joinWay2);

        // when
        List<JoinWay> foundJoinWays = joinWayService.findJoinWays(product.getId());

        // then
        Assertions.assertThat(foundJoinWays.size()).isEqualTo(2);
    }

    @Test
    public void verifyProductExistsTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> joinWayService.findJoinWays(99L));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Product does not exist");
    }
}