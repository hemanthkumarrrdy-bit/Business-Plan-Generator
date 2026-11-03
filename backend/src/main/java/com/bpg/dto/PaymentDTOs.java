package com.bpg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public class PaymentDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateOrderRequest {
        private String planType; // "STARTER", "PROFESSIONAL", "ENTERPRISE"
        private String billingPeriod; // "monthly", "yearly"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderResponseDTO {
        private String orderId;
        private BigDecimal amount;
        private String currency;
        private String keyId;
        private boolean mockMode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VerifyPaymentRequest {
        private String razorpayOrderId;
        private String razorpayPaymentId;
        private String razorpaySignature;
        private String planType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentVerificationResponse {
        private boolean success;
        private String message;
        private String currentPlan;
    }
}
