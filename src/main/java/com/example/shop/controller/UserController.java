package com.example.shop.controller;


import com.example.shop.dtos.request.UserRequest;
import com.example.shop.dtos.response.ApiResponse;
import com.example.shop.dtos.response.UserResponse;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/current-user")
	public ApiResponse<UserResponse> getCurrentUser()
	{
		UserResponse result = userService.getCurrentUser();
		return  ApiResponse.<UserResponse>builder()
				.message("Get current user success")
				.result(result)
				.build();
	}

	@GetMapping()
	public ApiResponse<List<UserResponse>> getAllUser()
	{
		List<UserResponse> result = userService.getAll();
		return  ApiResponse.<List<UserResponse>>builder()
				.message("Get all users success")
				.result(result)
				.build();
	}

	@PutMapping("/{userId}")
	public ApiResponse<UserResponse> update(@PathVariable("userId") Long userId, @RequestBody UserRequest request)
	{
		UserResponse result= userService.update(userId,request);
		return  ApiResponse.<UserResponse>builder()
				.message("Update user success")
				.result(result)
				.build();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/register")
	public ApiResponse<UserResponse> create(@RequestBody UserRequest request)
	{
		UserResponse result= userService.create(request);
		return  ApiResponse.<UserResponse>builder()
				.message("Create user success")
				.result(result)
				.build();
	}

}
