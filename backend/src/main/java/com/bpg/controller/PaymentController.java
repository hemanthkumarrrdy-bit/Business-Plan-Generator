package com.bpg.controller;

import com.bpg.dto.PaymentDTOs.*;
import com.bpg.security.JwtUtil;
import com.bpg.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtUtil jwtUtil;

    private Long getUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized");
        }
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestHeader("Authorization") String auth,
            @RequestBody CreateOrderRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(paymentService.createOrder(userId, req));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerificationResponse> verify(
            @RequestHeader("Authorization") String auth,
            @RequestBody VerifyPaymentRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(paymentService.verifyPayment(userId, req));
    }
}
