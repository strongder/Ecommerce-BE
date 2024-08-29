package com.example.shop.service;


import com.example.shop.config.payment.VnpayConfig;
import com.example.shop.dtos.request.PaymentRequest;
import com.example.shop.dtos.response.PaymentResponse;
import com.example.shop.dtos.response.VnpayResponse;
import com.example.shop.exception.AppException;
import com.example.shop.exception.ErrorResponse;
import com.example.shop.model.Order;
import com.example.shop.model.Payment;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.PaymentRepository;
import com.example.shop.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentService {

    VnpayConfig vnpayConfig;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    ModelMapper modelMapper;

    @Transactional
    public VnpayResponse createVnPayPayment(PaymentRequest paymentRequest, HttpServletRequest request) {
        // Lấy thông tin từ PaymentRequest
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorResponse.ORDER_NOT_EXISTED));
        long amount = (long) (order.getTotal() * 100); // Chuyển đổi từ Đơn vị tiền tệ
        String bankCode = request.getParameter("bankCode");
        // Tạo và lưu giao dịch Payment vào CSDL
        Payment payment = new Payment();
        payment.setAmount((double) amount / 100);
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setStatus("PENDING");
        payment.setPaymentTime(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        // Lưu transactionId cho giao dịch Payment
        String transactionId = VnpayUtil.getRandomNumber(8); // Tạo một transactionId ngẫu nhiên
        payment.setTransactionId(transactionId);
        payment.setOrder(order);
        paymentRepository.save(payment);

        // Tạo Map các tham số cho VNPay
        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toán đơn hàng #" + paymentRequest.getOrderId());
        vnpParamsMap.put("vnp_TxnRef", transactionId); // Mã giao dịch
        vnpParamsMap.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }

        // Tạo URL thanh toán
        String queryUrl = VnpayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnpayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // Trả về VnpayResponse với URL thanh toán
        return VnpayResponse.builder()
                .code("00") // Thành công
                .message("Tạo thanh toán thành công")
                .paymentUrl(paymentUrl)
                .build();
    }


    public List<PaymentResponse> getAll()
    {
        return paymentRepository.findAll().stream().map(payment
                -> modelMapper.map(payment, PaymentResponse.class))
                .collect(Collectors.toList());
    }


    public VnpayResponse handlePaymentCallback(String status, String transactionId) {
        if (status == null || transactionId == null) {
            throw new AppException(ErrorResponse.INVALID_REQUEST_PARAMETERS);
        }
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorResponse.TRANSACTION_NOT_EXISTED));
        VnpayResponse vnpayResponse;
        if (status.equals("00")) {
            payment.setStatus("SUCCESS");
            payment.getOrder().setStatus("PAID");
            vnpayResponse = VnpayResponse.builder()
                    .code("00")
                    .message("Payment successfully")
                    .paymentUrl("") // Chưa cần sử dụng trường này trong callback
                    .build();
        } else {
            payment.setStatus("failed");
            vnpayResponse = null;
        }
        paymentRepository.save(payment);
        return vnpayResponse;
    }

}

