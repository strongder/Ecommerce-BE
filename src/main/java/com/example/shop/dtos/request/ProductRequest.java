package com.example.shop.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductRequest {

    private Long id;
    private String name ;
    private List<String> imageUrls;
    private String category;
    private String description;
    private List<VarProductRequest> varProducts;
    private Double price;
}
