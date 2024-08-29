 package com.example.shop.controller;

 import com.example.shop.dtos.request.CartItemRequest;
 import com.example.shop.dtos.response.CartResponse;
 import com.example.shop.service.CartService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/carts")
@RestController
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<CartResponse> addProductByCart(
			@PathVariable("userId") Long userId,
			@RequestBody CartItemRequest request
			)
	{
		CartResponse result = cartService.addProductToCart(userId, request);
		return new ResponseEntity<>(
				result, HttpStatus.CREATED);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<CartResponse> getProductByCart(
			@PathVariable("userId") Long userId)
	{
		CartResponse result = cartService.getByUserId(userId);
		return new ResponseEntity<>(
				result, HttpStatus.OK);
	}

	@PutMapping("/remove-cart-item")
	public ResponseEntity<CartResponse> removeCartItem(
			@RequestParam("userId") Long userId,
			@RequestParam("cartItemId") Long cartItemId
			)
	{
		CartResponse result = cartService.removeCartItem(userId, cartItemId);
		return new ResponseEntity<>(
				result, HttpStatus.OK);
	}

	@PutMapping("/update-cart-item")
	public ResponseEntity<CartResponse> updateCartItem(
			@RequestParam("userId") Long userId,
			@RequestParam("cartItemId") Long cartItemId,
			@RequestParam("quantity") int quantity
			)
	{
		CartResponse result = cartService.updateProductInCart(userId, cartItemId, quantity);
		return new ResponseEntity<>(
				result, HttpStatus.OK);
	}
}
