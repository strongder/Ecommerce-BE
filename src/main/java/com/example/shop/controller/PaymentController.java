package com.example.shop.controller;

import com.example.shop.dtos.request.PaymentRequest;
import com.example.shop.dtos.response.ApiResponse;
import com.example.shop.dtos.response.PaymentResponse;
import com.example.shop.dtos.response.VnpayResponse;
import com.example.shop.repository.PaymentRepository;
import com.example.shop.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    PaymentRepository paymentRepository;

    @PostMapping("/vn-pay")
    public ApiResponse<VnpayResponse> createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        return  ApiResponse.<VnpayResponse>builder()
                .result(paymentService.createVnPayPayment(paymentRequest, request))
                .message("Create payment successfully")
                .build();
    }

 //   @GetMapping("/vn-pay-callback")
//    public ApiResponse<VnpayResponse> payCallbackHandler(HttpServletRequest request) {
//        String status = request.getParameter("vnp_ResponseCode");
//        String transactionId = request.getParameter("vnp_TxnRef");
//
//        Payment payment = paymentRepository.findByTransactionId(transactionId).orElseThrow(
//                () -> new AppException(ErrorResponse.TRANSACTION_NOT_EXISTED)
//        );
//        ApiResponse  apiResponse;
//        if (status.equals("00")) {
//            payment.getOrder().setStatus("PAID");
//            payment.setStatus("success");
//              apiResponse = ApiResponse.<VnpayResponse>builder()
//                    .result(VnpayResponse.builder()
//                            .code("00")
//                            .message("Payment successfully")
//                            .paymentUrl("")
//                            .build())
//                    .message("Payment successfully")
//                    .build();
//        } else {
//            payment.setStatus("failed");
//              apiResponse = ApiResponse.<VnpayResponse>builder()
//                    .result(null)
//                    .message("Payment failed")
//                    .build();
//        }
//        paymentRepository.save(payment);
//        return apiResponse;
//
//    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<VnpayResponse> payCallbackHandler(HttpServletRequest request) {
        // Lấy tham số từ yêu cầu
        String status = request.getParameter("vnp_ResponseCode");
        String transactionId = request.getParameter("vnp_TxnRef");

        // Gọi phương thức từ service để xử lý callback
        VnpayResponse vnpayResponse = paymentService.handlePaymentCallback(status, transactionId);
        String message = status.equals("00") ? "Payment successfully" : "Payment failed";
        return ApiResponse.<VnpayResponse>builder()
                .result(vnpayResponse)
                .message(message).build();
    }


    @GetMapping()
    public ApiResponse<List<PaymentResponse>> getAllPayment() {
        return ApiResponse.<List<PaymentResponse>>builder()
                .message("Get all payment success")
                .result(paymentService.getAll())
                .build();
    }
}
