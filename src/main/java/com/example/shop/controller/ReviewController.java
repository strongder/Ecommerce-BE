package com.example.shop.controller;

import com.example.shop.dtos.request.ReviewRequest;
import com.example.shop.dtos.response.ApiResponse;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {

    ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponse> creatReview(@RequestBody ReviewRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .message("Create review success")
                .result(reviewService.createReview(request))
                .build();
    }


    @GetMapping("/product/{productId}")
    public ApiResponse<List<ReviewResponse>> getReviewByProductId(@PathVariable  Long productId){
        return ApiResponse.<List<ReviewResponse>>builder()
                .message("Get review by product id success")
                .result(reviewService.getReviewByProductId(productId))
                .build();
    }
}
