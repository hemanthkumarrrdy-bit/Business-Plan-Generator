package com.bpg;

import com.bpg.dto.FinancialDTOs.*;
import com.bpg.financial.FinancialEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FinancialEngineTest {

    private FinancialEngine engine;

    @BeforeEach
    void setUp() {
        engine = new FinancialEngine();
    }

    @Test
    void testBreakEvenCalculation_PositiveMargin() {
        BigDecimal price = BigDecimal.valueOf(100);
        BigDecimal varCost = BigDecimal.valueOf(30);
        BigDecimal fixedCosts = BigDecimal.valueOf(7000);

        BreakEvenAnalysisDTO result = engine.calculateBreakEven(price, varCost, fixedCosts);

        assertNotNull(result);
        assertEquals(0, BigDecimal.valueOf(70).compareTo(result.getContributionMargin()));
        // Break-even units = 7000 / 70 = 100 units
        assertEquals(0, BigDecimal.valueOf(100).compareTo(result.getBreakEvenUnitsMonthly()));
        // Break-even revenue = 100 * 100 = 10,000
        assertEquals(0, BigDecimal.valueOf(10000).compareTo(result.getBreakEvenRevenueMonthly()));
    }

    @Test
    void testBreakEvenCalculation_NegativeOrZeroMargin() {
        BigDecimal price = BigDecimal.valueOf(50);
        BigDecimal varCost = BigDecimal.valueOf(60);
        BigDecimal fixedCosts = BigDecimal.valueOf(5000);

        BreakEvenAnalysisDTO result = engine.calculateBreakEven(price, varCost, fixedCosts);

        assertNotNull(result);
        assertTrue(result.getContributionMargin().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(result.getStatusNote().contains("Warning"));
        assertEquals(0, BigDecimal.ZERO.compareTo(result.getBreakEvenUnitsMonthly()));
    }

    @Test
    void testFullFinancialModelGeneration() {
        FinancialInputsDTO inputs = FinancialInputsDTO.builder()
                .currency("USD")
                .initialInvestment(BigDecimal.valueOf(50000))
                .monthlyOperatingExpenses(BigDecimal.valueOf(5000))
                .expectedMonthlyUnits(BigDecimal.valueOf(120))
                .averageSellingPrice(BigDecimal.valueOf(100))
                .variableCostPerUnit(BigDecimal.valueOf(25))
                .forecastYears(3)
                .build();

        FullFinancialModelDTO model = engine.calculateFullModel(inputs);

        assertNotNull(model);
        assertEquals(3, model.getProjections().size());
        assertEquals(3, model.getScenarios().size());
        assertEquals(12, model.getMonthlyCashFlowChartData().size());
        assertTrue(model.getTotalStartupCost().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(model.getMetrics().getGrossProfitMargin());
    }
}
