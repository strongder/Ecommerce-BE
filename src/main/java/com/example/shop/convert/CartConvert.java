package com.example.shop.convert;

import com.example.shop.dtos.request.CartItemRequest;
import com.example.shop.dtos.response.CartResponse;
import com.example.shop.model.Cart;
import com.example.shop.model.CartItem;
import com.example.shop.model.Product;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CartConvert {
    ModelMapper modelMapper;
    ProductRepository productRepository;

    public CartItem cartItemConvertToEntity (CartItemRequest request) {
        CartItem cartItem= modelMapper.map(request, CartItem.class);
        return cartItem;
    }

    public CartResponse cartConvertToDTO(Cart cart) {
        // Lọc các CartItem có isDelete = false
        Set<CartItem> filteredCartItems = cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDelete())
                .collect(Collectors.toSet());
        cart.setCartItems(filteredCartItems);
        // Ánh xạ Cart sang CartResponse
        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        // Thực hiện các thao tác bổ sung trên các CartItem còn lại
        cartResponse.getCartItems().forEach(cartItem -> {
            Product product = productRepository.findByVarProductsId(cartItem.getVarProduct().getId());
            cartItem.setName(product.getName());
            cartItem.setPrice(product.getPrice());
            cartItem.setImage(product.getImageUrls().get(0).getImageUrl());
        });
        return cartResponse;
    }
}
