package com.example.shop.repository;

import com.example.shop.model.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
}
