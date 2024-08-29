package com.example.shop.controller;

import com.example.shop.dtos.request.ProductRequest;
import com.example.shop.dtos.response.ApiResponse;
import com.example.shop.dtos.response.ProductResponse;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable("id") Long id) {
        ProductResponse product = productService.getById(id);
        return  ApiResponse.<ProductResponse>builder()
                .message("Get product by id success")
                .result(product)
                .build();
    }

    @GetMapping("/search/{key}")
    public ApiResponse<List<ProductResponse>> getProductByKey(@PathVariable("key") String key) {
        List<ProductResponse> product = productService.getProductByKey(key);
        return ApiResponse.<List<ProductResponse>>builder()
                .message("Get product by key success")
                .result(product)
                .build();
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getAll(
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
        List<ProductResponse> productPage = productService.getAll(pageNum, pageSize, sortDir, sortBy);
            return ApiResponse.<List<ProductResponse>>builder()
                    .message("Get all product success")
                    .result(productPage)
                    .build();
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        List<ProductResponse> result = productService.getProductsByCategory(categoryId, pageNum, pageSize, sortDir, sortBy);
        return ApiResponse.<List<ProductResponse>>builder()
                .message("Get product by category success")
                .result(result)
                .build();
    }

    //	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
        ProductResponse result = productService.create(request);
        return ApiResponse.<ProductResponse>builder()
                .message("Create product success")
                .result(result)
                .build();
    }

    //	@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable("id") Long id,
            @RequestBody ProductRequest request) {
        ProductResponse result = productService.update(id, request);
        return ApiResponse.<ProductResponse>builder()
                .message("Update product success")
                .result(result)
                .build();
    }

}
