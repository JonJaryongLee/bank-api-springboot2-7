package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void findProductsTest() {
        // given
        Product product1 = new Product();
        product1.setFinPrdtNm("testprdtnm1");
        product1.setDclsMonth("testdclsmonth");
        product1.setKorCoNm("testcorconm");
        product1.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setFinPrdtNm("testprdtnm1");
        product2.setDclsMonth("testdclsmonth");
        product2.setKorCoNm("testcorconm");
        product2.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product2);

        // when
        List<Product> foundProducts = productService.findProducts();

        // then
        Assertions.assertThat(foundProducts.size()).isEqualTo(2);
    }

    @Test
    public void findProductTest() {
        // given
        Product product = new Product();
        product.setFinPrdtNm("testprdtnm1");
        product.setDclsMonth("testdclsmonth");
        product.setKorCoNm("testcorconm");
        product.setJoinDeny(Product.JoinDeny.제한없음);
        productRepository.save(product);

        // when
        Product foundProduct = productService.findProduct(product.getId());

        // then
        Assertions.assertThat(foundProduct.getId()).isEqualTo(product.getId());
        Assertions.assertThat(foundProduct.getFinPrdtNm()).isEqualTo(product.getFinPrdtNm());
        Assertions.assertThat(foundProduct.getDclsMonth()).isEqualTo(product.getDclsMonth());
        Assertions.assertThat(foundProduct.getKorCoNm()).isEqualTo(product.getKorCoNm());
        Assertions.assertThat(foundProduct.getJoinDeny()).isEqualTo(product.getJoinDeny());
    }

    @Test
    public void verifyProductExistsTest() {
        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> {
            productService.findProduct(99L);
        });

        // then
        Assertions.assertThat(e.getMessage()).isEqualTo("Product does not exist");
    }
}