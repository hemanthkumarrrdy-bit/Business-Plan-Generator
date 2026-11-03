package com.bpg.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String planType; // FREE, STARTER, PROFESSIONAL, ENTERPRISE

    @Column(nullable = false, length = 50)
    private String status; // ACTIVE, CANCELLED, EXPIRED

    @Builder.Default
    private String billingPeriod = "monthly";

    private String providerSubscriptionId;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
