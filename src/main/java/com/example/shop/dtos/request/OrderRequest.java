package com.example.shop.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderRequest {
	private Long id;
	private Long userId;
	private Long addressId;
	private String paymentMethod;
	//private Set<OrderItemRequest> orderItems;
	private String status;
}
