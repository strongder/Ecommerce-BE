package com.example.shop.controller;

import com.example.shop.dtos.request.CategoryRequest;
import com.example.shop.dtos.response.ApiResponse;
import com.example.shop.dtos.response.CategoryResponse;
import com.example.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public ApiResponse<List<CategoryResponse>> getAll()
	{
		List<CategoryResponse> result = categoryService.getAll();
		return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories success")
                .result(result)
                .build();
	}
	@GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable("id") Long id) {
        CategoryResponse result = categoryService.getById(id);
        return  ApiResponse.<CategoryResponse>builder()
                .message("Get category by id success")
                .result(result)
                .build();
    }
    @PostMapping()
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest request) {
        CategoryResponse result = categoryService.create(request);
        return ApiResponse.<CategoryResponse>builder()
                .message("Create category success")
                .result(result)
                .build();
    }

}
