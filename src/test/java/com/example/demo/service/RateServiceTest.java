package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Rate;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RateRepository;
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
class RateServiceTest {

    @Autowired
    RateService rateService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RateRepository rateRepository;

    @Test
    public void findRatesTest() {
        // given
        Product product = new Product();
        product.setFinPrdtNm("testprdtnm1");
        product.setDclsMonth("testdclsmonth");
        product.setKorCoNm("testcorconm");
        product.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product);

        Rate rate1 = new Rate();
        rate1.setProduct(product);
        rate1.setSaveTrm(6L);
        rate1.setInitRate(99.99);
        rate1.setInitRate2(999.999);
        rateRepository.save(rate1);

        Rate rate2 = new Rate();
        rate2.setProduct(product);
        rate2.setSaveTrm(12L);
        rate2.setInitRate(88.88);
        rate2.setInitRate2(888.888);
        rateRepository.save(rate2);

        // when
        List<Rate> foundRates = rateService.findRates(product.getId());

        // then
        Assertions.assertThat(foundRates.size()).isEqualTo(2);
    }

    @Test
    public void verifyProductExistsTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> rateService.findRates(99L));

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Product does not exist");
    }
}