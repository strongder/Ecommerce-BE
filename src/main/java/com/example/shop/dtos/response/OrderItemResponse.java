package com.example.shop.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
        private Long id;
        private int quantity;
        private Double price;
        private String name ;
        private String image;
        private VarProductResponse varProduct;
}
