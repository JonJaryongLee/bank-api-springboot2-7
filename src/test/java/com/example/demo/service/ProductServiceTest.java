//package com.example.demo.service;
//
//import com.example.demo.entity.Product;
//import com.example.demo.entity.ProductOption;
//import com.example.demo.repository.ProductOptionRepository;
//import com.example.demo.repository.ProductRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//class ProductServiceTest {
//
//    @Autowired
//    ProductService productService;
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    ProductOptionRepository productOptionRepository;
//
//    private Product product1;
//    private Product product2;
//    private Product product3;
//
//    private ProductOption productOption1;
//    private ProductOption productOption2;
//
//    @BeforeEach
//    public void setUp() {
//        product1 = new Product();
//        product1.setFinPrdtCd("testFinPrdtCd1");
//        product1.setDclsMonth("testDclsMonth1");
//        product1.setKorCoNm("testKorCoNm1");
//        product1.setFinPrdtNm("testFinPrdtNm1");
//        product1.setEtcNote("testEtcNote1");
//        product1.setJoinDeny(Product.JoinDeny.서민전용);
//        product1.setJoinMember(Product.JoinMember.만50세이상);
//        product1.setJoinWay(Product.JoinWay.스마트폰);
//        productRepository.save(product1);
//
//        productOption1 = new ProductOption();
//        productOption1.setProduct(product1);
//        productOption1.setIntrRate(22.22);
//        productOption1.setIntrRate2(33.33);
//        productOption1.setIntrRateType(ProductOption.IntrRateType.단리);
//        productOption1.setSaveTrm(ProductOption.SaveTrm.TWELVE);
//        productOptionRepository.save(productOption1);
//
//        product2 = new Product();
//        product2.setFinPrdtCd("testFinPrdtCd2");
//        product2.setDclsMonth("testDclsMonth2");
//        product2.setKorCoNm("testKorCoNm2");
//        product2.setFinPrdtNm("testFinPrdtNm2");
//        product2.setEtcNote("testEtcNote2");
//        product2.setJoinDeny(Product.JoinDeny.제한없음);
//        product2.setJoinMember(Product.JoinMember.실명의개인);
//        product2.setJoinWay(Product.JoinWay.영업점);
//        productRepository.save(product2);
//
//        productOption2 = new ProductOption();
//        productOption2.setProduct(product2);
//        productOption2.setIntrRate(44.44);
//        productOption2.setIntrRate2(55.55);
//        productOption2.setIntrRateType(ProductOption.IntrRateType.복리);
//        productOption2.setSaveTrm(ProductOption.SaveTrm.SIX);
//        productOptionRepository.save(productOption2);
//
//        product3 = new Product();
//        product3.setFinPrdtCd("testFinPrdtCd3");
//        product3.setDclsMonth("testDclsMonth3");
//        product3.setKorCoNm("testKorCoNm3");
//        product3.setFinPrdtNm("testFinPrdtNm3");
//        product3.setEtcNote("testEtcNote3");
//        product3.setJoinDeny(Product.JoinDeny.제한없음);
//        product3.setJoinMember(Product.JoinMember.실명의개인);
//        product3.setJoinWay(Product.JoinWay.영업점);
//        productRepository.save(product3);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        productOptionRepository.deleteAll();
//        productRepository.deleteAll();
//    }
//
//    @Test
//    public void findProductsTest() {
//        // given
//
//        // when
//        List<Product> products = productService.findProducts();
//
//        // then
//        Assertions.assertThat(products.size()).isEqualTo(3L);
//    }
//
//    @Test
//    public void findProductTest() {
//        // given
//
//        // when
//        ProductWithOption foundProduct = productService.findProduct(product1.getFinPrdtCd());
//
//        // then
//        Assertions.assertThat(foundProduct.getId()).isEqualTo(product1.getId());
//        Assertions.assertThat(foundProduct.getFinPrdtCd()).isEqualTo("testFinPrdtCd1");
//        Assertions.assertThat(foundProduct.getJoinMember()).isEqualTo(Product.JoinMember.만50세이상);
//        Assertions.assertThat(foundProduct.getIntrRate()).isEqualTo(22.22);
//        Assertions.assertThat(foundProduct.getSaveTrm()).isEqualTo(ProductOption.SaveTrm.TWELVE);
//    }
//
//    @Test
//    public void verifyProductExistsTest() {
//        // when
//        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
//            productService.findProduct("ddddddd");
//        });
//
//        // then
//        String expectedMessage = "Product does not exist";
//        String actualMessage = exception.getMessage();
//        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);
//    }
//
//    @Test
//    public void verifyProductOptionExistsTest() {
//        // when
//        ProductWithOption productWithoutOption = productService.findProduct(product3.getFinPrdtCd());
//
//        // then
//        Assertions.assertThat(productWithoutOption.getId()).isEqualTo(product3.getId());
//        Assertions.assertThat(productWithoutOption.getFinPrdtCd()).isEqualTo("testFinPrdtCd3");
//        Assertions.assertThat(productWithoutOption.getJoinMember()).isEqualTo(Product.JoinMember.실명의개인);
//        Assertions.assertThat(productWithoutOption.getIntrRate()).isNull();
//        Assertions.assertThat(productWithoutOption.getSaveTrm()).isNull();
//    }
//}