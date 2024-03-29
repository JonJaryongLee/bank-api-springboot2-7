package com.example.demo.service;

import com.example.demo.entity.Product;
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

    /**
     * 모든 제품을 조회합니다.
     *
     * @return 제품 목록
     */
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 productNO 에 해당하는 상품을 조회합니다.
     *
     * @param productNo 상품 코드
     * @return 해당 상품
     * @throws NoSuchElementException 상품이 존재하지 않을 경우
     */
    public Product findProduct(Long productNo) {
        Product product = verifyProductExists(productNo);
        return product;
    }

    private Product verifyProductExists(Long productNo) {
        return productRepository.findById(productNo).orElseThrow(
                () -> new NoSuchElementException("Product does not exist"));
    }
}
