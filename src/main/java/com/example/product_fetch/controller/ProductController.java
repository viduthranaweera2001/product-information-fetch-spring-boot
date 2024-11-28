package com.example.product_fetch.controller;

import com.example.product_fetch.model.ProductInfo;
import com.example.product_fetch.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductInfoService productInfoService;

    @GetMapping("/product-info")
    public ProductInfo getProductInfo(@RequestParam String url) {
        return productInfoService.extractProductInfo(url);
    }
}