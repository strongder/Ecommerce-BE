package com.example.shop.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private Long id;
    private String comment;
    private int rating;
    private Long userId;
    private Long productId;

}
