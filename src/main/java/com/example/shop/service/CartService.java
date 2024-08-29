package com.example.shop.service;

import com.example.shop.convert.CartConvert;
import com.example.shop.dtos.request.CartItemRequest;
import com.example.shop.dtos.response.CartResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.Cart;
import com.example.shop.model.CartItem;
import com.example.shop.model.VarProduct;
import com.example.shop.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@RequiredArgsConstructor
public class CartService{

	 CartRepository cartRepository;
	 VarProductRepository varProductRepository;
	 ProductRepository productRepository;
	 ProductService productService;
	 UserRepository userRepository;
	 CartConvert cartConvert;
	 CartItemRepository cartItemRepository;

	@Transactional
	public CartResponse addProductToCart(Long userId, CartItemRequest request) {

		VarProduct varProduct = varProductRepository.findById(request.getVarProductId())
				.orElseThrow(() -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED));

		boolean isOutOfStock = productService.isProductInStock(request.getVarProductId(), request.getQuantity());
		if (!isOutOfStock) {
			throw new AppException(ErrorResponse.OUT_OF_STOCK);
		}

		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		CartItem cartItem = cartConvert.cartItemConvertToEntity(request);
		Optional<CartItem> existingCartItem = cart.getCartItems().stream()
				.filter(item -> item.getVarProduct().getId().equals(request.getVarProductId())&& !item.isDelete())
				.findFirst();

		if (existingCartItem.isPresent()) {
			existingCartItem.get().setQuantity(request.getQuantity() + existingCartItem.get().getQuantity());
			cartItemRepository.save(existingCartItem.get());
		} else {
			cartItem.setVarProduct(varProduct);
			cartItem.setQuantity(request.getQuantity());
			cartItem.setCart(cart);
			cartItemRepository.save(cartItem);
			cart.getCartItems().add(cartItem);
		}

		// Tính tổng giá trị đơn hàng
		Double total = calculateTotalPrice(cart.getCartItems());
		cart.setTotal(total);
		cart.setNumberProduct(getNumberProduct(userId));
		cartRepository.save(cart);

		return cartConvert.cartConvertToDTO(cart);
	}

	public Integer getNumberProduct(Long userId) {
		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		return cart.getCartItems().stream().filter(item -> !item.isDelete()).collect(Collectors.toSet()).size();
	}
	public Double calculateTotalPrice(Set<CartItem> cartItems) {
		return cartItems.stream()
				.filter(cartItem -> !cartItem.isDelete())
				.mapToDouble(cartItem -> cartItem.getQuantity() * cartItem.getVarProduct().getProduct().getPrice())
				.sum();
	}

	public void clearCart(Long id) {
		Cart cart = cartRepository.findById(id).orElse(null);
		Set<CartItem> list = cart.getCartItems();
		for (CartItem cartItem : list) {
			if(!cartItem.isDelete()) {
				cartItem.setDelete(true);
				VarProduct variantProduct = cartItem.getVarProduct();
				variantProduct.setStock(variantProduct.getStock()-cartItem.getQuantity());
				varProductRepository.save(variantProduct);
				cartItemRepository.save(cartItem);
			}
		}
		cart.setTotal(0.0);
		cart.setNumberProduct(0);
		cartRepository.save(cart);
	}

	public  CartResponse getByUserId(Long userId)
	{
		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		return cartConvert.cartConvertToDTO(cart);
	}

	public CartResponse removeCartItem(Long userId, Long cartItemId) {
		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		cartItemRepository.deleteById(cartItemId);
		cart.setNumberProduct(getNumberProduct(userId));
		cart.setTotal(calculateTotalPrice(cart.getCartItems()));
		cartRepository.save(cart);
		return cartConvert.cartConvertToDTO(cart);
	}

	public CartResponse updateProductInCart(Long userId, Long cartItemId, int quantity){
		Cart cart = cartRepository.findByUserId(userId).orElse(null);
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);

		if(quantity == 0){
			removeCartItem(userId, cartItemId);
		}else
		{
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
			cart.setTotal(calculateTotalPrice(cart.getCartItems()));
			cart.setNumberProduct(getNumberProduct(userId));
			cartRepository.save(cart);
		}
		return cartConvert.cartConvertToDTO(cart);
	}
}
