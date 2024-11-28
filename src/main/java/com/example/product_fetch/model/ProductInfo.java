package com.example.product_fetch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    private String brand;
}
