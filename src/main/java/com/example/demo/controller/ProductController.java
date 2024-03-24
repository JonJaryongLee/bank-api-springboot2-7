package com.example.demo.controller;

import com.example.demo.controller.response.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public Result listProduct() {
        List<Product> foundProducts = productService.findProducts();
        List<ProductDto> collect = foundProducts.stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getDclsMonth(),
                        p.getKorCoNm(),
                        p.getFinPrdtNm(),
                        p.getJoinDeny().name(),
                        p.getSpclCnd()
                ))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
