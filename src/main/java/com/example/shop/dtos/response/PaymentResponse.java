package com.example.shop.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
//    private String code; // Mã phản hồi (00: thành công, các mã khác: lỗi)
//    private String message; // Thông báo hoặc mô tả ngắn gọn về kết quả giao dịch
    private String transactionId; // Mã giao dịch được hệ thống hoặc VNPay sinh ra
    private Long orderId; // ID của đơn hàng liên quan
    private Double amount; // Số tiền đã thanh toán
    private String paymentMethod; // Phương thức thanh toán (VNPay, PayPal, ...)
    private String status; // Trạng thái thanh toán (success, failed, pending)
    private LocalDateTime paymentTime;
}
