package com.example.shop.service;

import com.example.shop.convert.ProductConvert;
import com.example.shop.dtos.request.ProductRequest;
import com.example.shop.dtos.response.ProductResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.Category;
import com.example.shop.model.ImageProduct;
import com.example.shop.model.Product;
import com.example.shop.model.VarProduct;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.VarProductRepository;
import com.example.shop.utils.PaginationSortingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService{

	 ProductRepository productRepository;
	 VarProductRepository varProductRepository;
	 CategoryRepository categoryRepository;
     ProductConvert productConvert;
	 ReviewService reviewService;

	@Transactional(readOnly = true)
	public ProductResponse getById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			ProductResponse response = productConvert.convertToDTO(product.get());
			return response;//productConvert.convertToDTO(product.get());
		}
		throw new AppException(ErrorResponse.PRODUCT_NOT_EXISTED);
	}

	@Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId, int pageNum, int pageSize, String sortDir, String sortBy) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isPresent()) {
			Pageable pageable = PaginationSortingUtils.getPageable(pageNum, pageSize, sortDir, sortBy);
			Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
			// Chuyển đổi các đối tượng Product thành ProductResponse
			return productPage.stream().map(product -> productConvert.convertToDTO(product)).collect(Collectors.toList());
		}
		throw new AppException(ErrorResponse.CATEGORY_NOT_EXISTED);
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getAll(int pageNum, int pageSize, String sortDir, String sortBy) {
		Pageable pageable = PaginationSortingUtils.getPageable(pageNum, pageSize, sortDir, sortBy);
		Page<Product> productPage = productRepository.findAll(pageable);
		return productPage.stream().map(product -> productConvert.convertToDTO(product)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<ProductResponse> getProductByKey(String key) {

		List<Product> products = productRepository.findAll().stream().filter(
				product -> product.getName().contains(key)).collect(Collectors.toList());
		return products.stream().
				map(product -> productConvert.convertToDTO(product))
				.collect(Collectors.toList());
	}

	@Transactional
	public ProductResponse create(ProductRequest request) {
		Product product = productConvert.convertToEntity(request);
		product.setCreatedAt(LocalDateTime.now());
		List<ImageProduct> images = request.getImageUrls().stream()
				.map(imageUrl -> {
					ImageProduct image = new ImageProduct();
					image.setImageUrl(imageUrl);
					image.setProduct(product);
					return image;
				})
				.collect(Collectors.toList());
		product.setImageUrls(images);
		AtomicInteger stock = new AtomicInteger();
		product.setStock(stock.get());
		product.setRating(0.0f);
		productRepository.save(product);
		ProductResponse response = productConvert.convertToDTO(product);
		System.out.println(response);
		return response;
	}

	@Transactional
	public ProductResponse update(Long productId, ProductRequest request) {
		Optional<Product> existedProduct = productRepository.findById(productId);
		if (existedProduct.isPresent())
		{
			Product product = productConvert.convertToEntity(request);
			product.setUpdatedAt(LocalDateTime.now());
			product.setId(productId);
			List<ImageProduct> images = request.getImageUrls().stream()
					.map(imageUrl -> {
						ImageProduct image = new ImageProduct();
						image.setImageUrl(imageUrl);
						image.setProduct(product);
						return image;
					})
					.collect(Collectors.toList());
			product.setImageUrls(images);
			AtomicInteger stock = new AtomicInteger();
			product.setStock(stock.get());
			productRepository.save(product);
			return productConvert.convertToDTO(product);
		}
		else {
			throw new AppException(ErrorResponse.PRODUCT_NOT_EXISTED);
		}
	}


	public boolean isProductInStock(Long varProductId, int quantity) {
		Optional<VarProduct> varProduct = varProductRepository.findById(varProductId);
		if (varProduct.isPresent()) {
			return varProduct.get().getStock() >= quantity;
		}
		return false;
	}
}
