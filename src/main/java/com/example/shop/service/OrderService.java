package com.example.shop.service;


import com.example.shop.dtos.request.OrderRequest;
import com.example.shop.dtos.response.OrderResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.*;
import com.example.shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderService {

	 OrderRepository orderRepository;
	 ModelMapper modelMapper;
	 ProductRepository productRepository;
     UserRepository userRepository;
	 CartRepository cartRepository;
	 CartService cartService;
     CartItemRepository cartItemRepository;
     AddressRepository addressRepository;


	@Transactional
	public OrderResponse placeOrder(OrderRequest orderRequest)
	{
		Order order = orderConvertToEntity(orderRequest);
		if(order.getUser().getCart().getNumberProduct() == 0)
		{
			throw new AppException(ErrorResponse.CART_EMPTY);
		}
		cartService.clearCart(order.getUser().getCart().getId());
		if(order.getPaymentMethod().equals("COD")) {
			order.setStatus("PENDING");
			orderRepository.save(order);
		}
		else if (order.getPaymentMethod().equals("VNPAY")) {
			order.setStatus("PENDING PAYMENT");
			orderRepository.save(order);
		}
		OrderResponse orderResponse = orderConvertToDTO(order);
		return orderResponse;
	}

	public Order orderConvertToEntity(OrderRequest request)
	{
		Order order = modelMapper.map(request, Order.class);
		User user = userRepository.findById(request.getUserId()).orElseThrow(
				()-> new AppException(ErrorResponse.USER_NOT_EXISTED));
		order.setUser(user);
		order.setOrderItems(convertCartItemToOrderItem(user.getCart().getCartItems()));
		order.setAddress(addressRepository.findById(request.getAddressId()).orElse(null));
		order.setTotal(user.getCart().getTotal());
		return order;
	}
	public OrderResponse orderConvertToDTO(Order order)
	{
		OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
		orderResponse.setRecipientName(order.getUser().getFullName());
		orderResponse.setPhone(order.getUser().getPhone());
		orderResponse.setPaymentMethod(order.getPaymentMethod());
		orderResponse.setStatus(order.getStatus());
		orderResponse.setTotal(order.getTotal());
		orderResponse.getOrderItems().forEach(
				orderItem -> {
					Product product = productRepository.findByVarProductsId(orderItem.getVarProduct().getId());
					orderItem.setName(product.getName());
					orderItem.setImage(product.getImageUrls().get(0).getImageUrl());
					orderItem.setPrice(product.getPrice());
				}
		);
		return orderResponse;
	}

	public Set<OrderItem> convertCartItemToOrderItem (Set<CartItem> cartItems)
	{
		Set<OrderItem> orderItems = new HashSet<>();
		for(CartItem cartItem : cartItems)
		{
			if(!cartItem.isDelete())
			{
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setVarProduct(cartItem.getVarProduct());
			orderItems.add(orderItem);}
		}
		return orderItems;
	}

	@Scheduled(fixedRate = 3600000) // Kiểm tra mỗi giờ (3600000 milliseconds = 1 giờ)
	public void checkPendingOrders() {
		Date now = new Date();
		List<Order> pendingOrders = orderRepository.findByStatus("PENDING PAYMENT");

		for (Order order : pendingOrders) {
			long diffInMillies = Math.abs(now.getTime() - order.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			long diffInHours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			if (diffInHours >= 24) { // Giả sử đơn hàng hết hạn sau 24 giờ
				order.setStatus("CANCELLED");
				orderRepository.save(order);
			}
		}
	}

}
