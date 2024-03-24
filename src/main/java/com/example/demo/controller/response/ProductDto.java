package com.example.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String dclsMonth;
    private String korCoNm;
    private String finPrdtNm;
    private String joinDeny;
    private String spclCnd;
}
