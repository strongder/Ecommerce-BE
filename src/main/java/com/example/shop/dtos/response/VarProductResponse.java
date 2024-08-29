package com.example.shop.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VarProductResponse {
    private Long id;
    private Map<String, Object> attribute;
    private int stock;
}
