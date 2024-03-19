package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOption;
import com.example.demo.repository.ProductOptionRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    /**
     * 모든 제품을 조회합니다.
     * @return 제품 목록
     */
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 finPrdtCd에 해당하는 상품을 조회합니다.
     * @param finPrdtCd 상품 코드
     * @return 해당 상품
     * @throws NoSuchElementException 상품이 존재하지 않을 경우
     */
    public ProductWithOption findProduct(String finPrdtCd) {
        ProductWithOption productWithOption = new ProductWithOption();

        Product product = verifyProductExists(finPrdtCd);
        ProductOption productOption = verifyProductOptionExists(product);

        productWithOption.setId(product.getId());
        productWithOption.setFinPrdtCd(product.getFinPrdtCd());
        productWithOption.setDclsMonth(product.getDclsMonth());
        productWithOption.setKorCoNm(product.getKorCoNm());
        productWithOption.setFinPrdtNm(product.getFinPrdtNm());
        productWithOption.setEtcNote(product.getEtcNote());
        productWithOption.setJoinDeny(product.getJoinDeny());
        productWithOption.setJoinMember(product.getJoinMember());
        productWithOption.setJoinWay(product.getJoinWay());
        productWithOption.setSpclCnd(product.getSpclCnd());
        productWithOption.setIntrRate(productOption.getIntrRate());
        productWithOption.setIntrRate2(productOption.getIntrRate2());
        productWithOption.setSaveTrm(productOption.getSaveTrm());

        return productWithOption;
    }

    /**
     * 주어진 finPrdtCd에 해당하는 상품이 존재하는지 확인합니다.
     * @param finPrdtCd 상품 코드
     * @return 해당 상품
     * @throws NoSuchElementException 상품이 존재하지 않을 경우
     */
    private Product verifyProductExists(String finPrdtCd) {
        return productRepository.findByFinPrdtCd(finPrdtCd).orElseThrow(
                () -> new NoSuchElementException("Product does not exist"));
    }

    /**
     * 주어진 product 에 option 이 존재하는지 확인하고, 옵션이 없을 경우 빈 객체를 전달합니다.
     * @param product 상품 객체
     * @return 상품 옵션
     */
    private ProductOption verifyProductOptionExists(Product product) {
        return productOptionRepository.findByProduct(product).orElseGet(() -> {
            ProductOption defaultOption = new ProductOption();
            return defaultOption;
        });
    }
}
