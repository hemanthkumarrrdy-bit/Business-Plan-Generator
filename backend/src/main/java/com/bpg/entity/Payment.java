package com.bpg.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long subscriptionId;

    @Builder.Default
    private String provider = "RAZORPAY";

    private String providerPaymentId;
    private String providerOrderId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Builder.Default
    private String currency = "INR";

    @Column(nullable = false)
    private String status; // CREATED, SUCCESS, FAILED

    @Builder.Default
    private Instant createdAt = Instant.now();
}
