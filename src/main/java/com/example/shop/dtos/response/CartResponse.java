package com.example.shop.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Long userId;
    private int numberProduct;
    private Set<CartItemResponse> cartItems;
    private double total;
}
