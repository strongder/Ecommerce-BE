package com.example.shop.controller;

import com.example.shop.dtos.request.OrderRequest;
import com.example.shop.dtos.response.OrderResponse;
import com.example.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        OrderResponse orderDTO = orderService.placeOrder(orderRequest);

        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
