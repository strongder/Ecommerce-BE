package com.example.shop.controller;


import com.example.shop.dtos.request.AuthRequest;
import com.example.shop.dtos.request.UserRequest;
import com.example.shop.dtos.response.AuthResponse;
import com.example.shop.dtos.response.UserResponse;
import com.example.shop.service.UserService;
import com.example.shop.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;


	// generate jwt
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> generateToken(@RequestBody AuthRequest authRequest)
	{
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			String token = JwtUtils.generateToken(authRequest.getUsername());
			AuthResponse authResponse = new AuthResponse(token, "Đăng nhập thành công");

			return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest request)
	{
		UserResponse result= userService.signUp(request);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

}
