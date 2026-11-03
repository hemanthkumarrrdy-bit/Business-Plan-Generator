package com.bpg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class FinancialDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FinancialInputsDTO {
        private String currency;
        private BigDecimal initialInvestment;
        private BigDecimal monthlyOperatingExpenses;
        private BigDecimal expectedMonthlyUnits;
        private BigDecimal averageSellingPrice;
        private BigDecimal variableCostPerUnit;
        private Integer employeeCount;
        private Integer forecastYears; // 3 or 5
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StartupCostItemDTO {
        private String category;
        private BigDecimal amount;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BreakEvenAnalysisDTO {
        private BigDecimal sellingPrice;
        private BigDecimal variableCostPerUnit;
        private BigDecimal contributionMargin;
        private BigDecimal contributionMarginRatio;
        private BigDecimal monthlyFixedCosts;
        private BigDecimal breakEvenUnitsMonthly;
        private BigDecimal breakEvenRevenueMonthly;
        private BigDecimal breakEvenUnitsAnnual;
        private BigDecimal breakEvenRevenueAnnual;
        private String statusNote;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnnualProjectionDTO {
        private int year;
        private BigDecimal revenue;
        private BigDecimal cogs;
        private BigDecimal grossProfit;
        private BigDecimal grossMarginPercent;
        private BigDecimal operatingExpenses;
        private BigDecimal ebitda;
        private BigDecimal depreciation;
        private BigDecimal taxes;
        private BigDecimal netProfit;
        private BigDecimal netMarginPercent;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FinancialMetricsDTO {
        private BigDecimal grossProfitMargin;
        private BigDecimal netProfitMargin;
        private BigDecimal operatingMargin;
        private BigDecimal monthlyBurnRate;
        private BigDecimal cashRunwayMonths;
        private BigDecimal estimatedRoiYear3;
        private String breakEvenPeriod;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScenarioResultDTO {
        private String scenarioName; // "Conservative", "Base", "Optimistic"
        private BigDecimal annualRevenueYear1;
        private BigDecimal annualRevenueYear2;
        private BigDecimal annualRevenueYear3;
        private BigDecimal annualNetProfitYear1;
        private BigDecimal annualNetProfitYear2;
        private BigDecimal annualNetProfitYear3;
        private BigDecimal breakEvenMonth;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FullFinancialModelDTO {
        private String currency;
        private BigDecimal totalStartupCost;
        private List<StartupCostItemDTO> startupCostBreakdown;
        private BreakEvenAnalysisDTO breakEven;
        private List<AnnualProjectionDTO> projections;
        private FinancialMetricsDTO metrics;
        private List<ScenarioResultDTO> scenarios;
        private List<Map<String, Object>> monthlyCashFlowChartData;
    }
}
