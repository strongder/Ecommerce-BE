package com.example.shop.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private int quantity;
    private Double price;
    private String name ;
    private String image;
    private boolean isDelete;
    private VarProductResponse varProduct;
}
