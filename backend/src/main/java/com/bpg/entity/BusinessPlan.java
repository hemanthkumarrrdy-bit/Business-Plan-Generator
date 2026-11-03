package com.bpg.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String businessName;

    private String industry;
    private String businessType;
    private String businessStage;
    private String country;

    @Builder.Default
    private String currency = "USD";

    @Builder.Default
    private String status = "DRAFT";

    @Column(columnDefinition = "TEXT")
    private String generationInputs;

    @Column(columnDefinition = "TEXT")
    private String planContent;

    @Column(columnDefinition = "TEXT")
    private String financialData;

    @OneToMany(mappedBy = "businessPlan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<BusinessPlanSection> sections = new ArrayList<>();

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
