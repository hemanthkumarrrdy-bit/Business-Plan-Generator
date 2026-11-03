package com.bpg.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class BusinessPlanDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePlanRequest {
        @NotBlank(message = "Business name is required")
        private String businessName;

        private String businessIdea;
        private String industry;
        private String businessType;
        private String businessStage;
        private String country;
        private String city;
        private String businessDescription;

        // Step 2: Target Market
        private String targetAudience;
        private String customerDemographics;
        private String geographicMarket;
        private String customerProblems;
        private String existingAlternatives;
        private String knownCompetitors;

        // Step 3: Business Model
        private String revenueModel;
        private String pricingStrategy;
        private String productsAndServices;
        private String distributionChannels;
        private String uniqueSellingProposition;
        private String competitiveAdvantages;

        // Step 4: Financial Inputs
        private String currency;
        private Double initialInvestment;
        private Double monthlyOperatingExpenses;
        private Double expectedMonthlySales;
        private Double averageSellingPrice;
        private Double variableCostPerUnit;
        private Integer employeeCount;
        private String forecastDuration;

        // Step 5: Preferences
        private String businessPlanPurpose;
        private String intendedAudience;
        private String detailLevel;
        private String writingStyle;
        private Boolean includeMarketResearch;
        private Boolean includeCompetitorAnalysis;
        private Boolean includeSwot;
        private Boolean includeFinancialProjections;
        private Boolean includeMarketingStrategy;
        private Boolean includeImplementationRoadmap;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSectionRequest {
        @NotBlank(message = "Content cannot be empty")
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SectionDTO {
        private Long id;
        private String sectionKey;
        private String sectionTitle;
        private String sectionContent;
        private Integer sortOrder;
        private Instant updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanSummaryDTO {
        private Long id;
        private String businessName;
        private String industry;
        private String businessType;
        private String businessStage;
        private String currency;
        private String status;
        private Instant createdAt;
        private Instant updatedAt;
        private int sectionCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FullPlanDTO {
        private Long id;
        private Long userId;
        private String businessName;
        private String industry;
        private String businessType;
        private String businessStage;
        private String country;
        private String currency;
        private String status;
        private Map<String, Object> generationInputs;
        private Map<String, Object> financialData;
        private List<SectionDTO> sections;
        private Instant createdAt;
        private Instant updatedAt;
    }
}
