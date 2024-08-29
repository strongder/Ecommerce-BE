package com.example.shop.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderResponse {

	private String recipientName;
	private String phone;
	private String paymentMethod;
	private String status;
	private Double total;
	private LocalDateTime createdAt;
	private AddressResponse address;
	private Set<OrderItemResponse> orderItems;
}