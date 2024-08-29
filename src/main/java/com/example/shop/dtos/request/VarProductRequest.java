package com.example.shop.dtos.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VarProductRequest {
    private Map<String, Object> attribute;
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;
}
