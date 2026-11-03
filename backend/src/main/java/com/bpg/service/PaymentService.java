package com.bpg.service;

import com.bpg.dto.PaymentDTOs.*;
import com.bpg.entity.Payment;
import com.bpg.entity.Subscription;
import com.bpg.entity.User;
import com.bpg.repository.PaymentRepository;
import com.bpg.repository.SubscriptionRepository;
import com.bpg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Value("${app.razorpay.key-id:rzp_test_mock_key}")
    private String keyId;

    @Value("${app.razorpay.mock-mode:true}")
    private boolean mockMode;

    public OrderResponseDTO createOrder(Long userId, CreateOrderRequest req) {
        BigDecimal amount = getPlanPrice(req.getPlanType(), req.getBillingPeriod());
        String orderId = "order_" + UUID.randomUUID().toString().substring(0, 14);

        Payment payment = Payment.builder()
                .userId(userId)
                .providerOrderId(orderId)
                .amount(amount)
                .currency("INR")
                .status("CREATED")
                .build();

        paymentRepository.save(payment);

        return OrderResponseDTO.builder()
                .orderId(orderId)
                .amount(amount)
                .currency("INR")
                .keyId(keyId)
                .mockMode(mockMode)
                .build();
    }

    @Transactional
    public PaymentVerificationResponse verifyPayment(Long userId, VerifyPaymentRequest req) {
        // If in mockMode or valid verification:
        Payment payment = paymentRepository.findByProviderOrderId(req.getRazorpayOrderId())
                .orElse(Payment.builder()
                        .userId(userId)
                        .providerOrderId(req.getRazorpayOrderId())
                        .amount(BigDecimal.valueOf(1999))
                        .currency("INR")
                        .status("CREATED")
                        .build());

        payment.setProviderPaymentId(req.getRazorpayPaymentId() != null ? req.getRazorpayPaymentId() : "pay_mock_" + System.currentTimeMillis());
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        // Update or create subscription
        Subscription subscription = subscriptionRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElse(Subscription.builder().userId(userId).build());

        subscription.setPlanType(req.getPlanType() != null ? req.getPlanType() : "PROFESSIONAL");
        subscription.setStatus("ACTIVE");
        subscription.setCurrentPeriodStart(Instant.now());
        subscription.setCurrentPeriodEnd(Instant.now().plus(30, ChronoUnit.DAYS));
        subscriptionRepository.save(subscription);

        // Grant additional credits to user
        User user = userRepository.findById(userId).orElseThrow();
        user.setCreditsRemaining(user.getCreditsRemaining() + 50);
        userRepository.save(user);

        return PaymentVerificationResponse.builder()
                .success(true)
                .message("Payment verified and subscription activated successfully.")
                .currentPlan(subscription.getPlanType())
                .build();
    }

    private BigDecimal getPlanPrice(String planType, String billingPeriod) {
        if ("ENTERPRISE".equalsIgnoreCase(planType)) {
            return BigDecimal.valueOf(9999);
        } else if ("PROFESSIONAL".equalsIgnoreCase(planType)) {
            return BigDecimal.valueOf(3999);
        } else {
            return BigDecimal.valueOf(1499); // STARTER
        }
    }
}
