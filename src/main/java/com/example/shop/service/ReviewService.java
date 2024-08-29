package com.example.shop.service;


import com.example.shop.dtos.request.ReviewRequest;
import com.example.shop.dtos.response.ReviewResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.Product;
import com.example.shop.model.Review;
import com.example.shop.model.User;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ReviewRepository;
import com.example.shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReviewService {

    ReviewRepository reviewRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ModelMapper modelMapper;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorResponse.USER_NOT_EXISTED)
        );
        Product product = productRepository.findById(request.getProductId()).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED)
        );
        Review review = modelMapper.map(request, Review.class);
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);
        product.setRating(getRatingByProductId(product.getId()));
        productRepository.save(product);
        return modelMapper.map(review, ReviewResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED));
        return reviews.stream().map(review ->
                modelMapper.map(review, ReviewResponse.class)).collect(Collectors.toList());
    }

    public Float getRatingByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED));
        // lấy ra những review có rating khác 0, sau đó tính trung bình
        return (float) reviews.stream().filter(review -> review.getRating() != 0)
                .mapToInt(Review::getRating).average().orElse(0);
    }
}
