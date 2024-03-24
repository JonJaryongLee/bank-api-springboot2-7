package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Rate;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RateService {

    private final RateRepository rateRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 번호에 해당하는 금리를 조회합니다.
     *
     * @param productNo 조회할 상품 번호
     * @return 상품 번호의 금리 리스트
     * @throws NoSuchElementException 만약 존재하지 않는 상품이라면 예외를 발생시킵니다.
     */
    public List<Rate> findRates(Long productNo) {
        Product product = verifyProductExists(productNo);
        List<Rate> foundRates = rateRepository.findByProduct(product);
        return foundRates;
    }

    private Product verifyProductExists(Long productNo) {
        return productRepository.findById(productNo).orElseThrow(
                () -> new NoSuchElementException("Product does not exist"));
    }
}
