package com.example.shop.repository;


import com.example.shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>{

	Page<Product> findByCategoryId(Long CategoryId, Pageable pageable);
	Product findByVarProductsId(Long varProductId);
}
