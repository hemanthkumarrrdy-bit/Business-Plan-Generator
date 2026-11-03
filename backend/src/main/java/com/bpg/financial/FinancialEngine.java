package com.bpg.financial;

import com.bpg.dto.FinancialDTOs.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class FinancialEngine {

    private static final RoundingMode RM = RoundingMode.HALF_UP;
    private static final int SCALE = 2;

    public FullFinancialModelDTO calculateFullModel(FinancialInputsDTO inputs) {
        String currency = inputs.getCurrency() != null ? inputs.getCurrency() : "USD";
        int years = (inputs.getForecastYears() != null && inputs.getForecastYears() == 5) ? 5 : 3;

        BigDecimal initialInv = defaultIfNull(inputs.getInitialInvestment(), BigDecimal.valueOf(25000));
        BigDecimal monthlyOpex = defaultIfNull(inputs.getMonthlyOperatingExpenses(), BigDecimal.valueOf(4000));
        BigDecimal monthlyUnits = defaultIfNull(inputs.getExpectedMonthlyUnits(), BigDecimal.valueOf(100));
        BigDecimal price = defaultIfNull(inputs.getAverageSellingPrice(), BigDecimal.valueOf(100));
        BigDecimal varCost = defaultIfNull(inputs.getVariableCostPerUnit(), BigDecimal.valueOf(30));
        int employees = inputs.getEmployeeCount() != null ? inputs.getEmployeeCount() : 2;

        // 1. Startup Cost Breakdown
        List<StartupCostItemDTO> startupCosts = calculateStartupCosts(initialInv);

        // 2. Break-Even Analysis
        BreakEvenAnalysisDTO breakEven = calculateBreakEven(price, varCost, monthlyOpex);

        // 3. Projections for 3 or 5 Years
        List<AnnualProjectionDTO> projections = calculateProjections(years, monthlyUnits, price, varCost, monthlyOpex, employees);

        // 4. Financial Metrics
        FinancialMetricsDTO metrics = calculateMetrics(initialInv, monthlyOpex, projections, breakEven);

        // 5. Scenario Analysis
        List<ScenarioResultDTO> scenarios = calculateScenarios(monthlyUnits, price, varCost, monthlyOpex, employees);

        // 6. Monthly Cash Flow points for charts (12 months of Year 1)
        List<Map<String, Object>> chartData = generateMonthlyChartData(monthlyUnits, price, varCost, monthlyOpex);

        return FullFinancialModelDTO.builder()
                .currency(currency)
                .totalStartupCost(initialInv)
                .startupCostBreakdown(startupCosts)
                .breakEven(breakEven)
                .projections(projections)
                .metrics(metrics)
                .scenarios(scenarios)
                .monthlyCashFlowChartData(chartData)
                .build();
    }

    private List<StartupCostItemDTO> calculateStartupCosts(BigDecimal total) {
        List<StartupCostItemDTO> list = new ArrayList<>();
        list.add(new StartupCostItemDTO("Technology & Software", total.multiply(BigDecimal.valueOf(0.20)).setScale(SCALE, RM), "Website, CRM, tools"));
        list.add(new StartupCostItemDTO("Branding & Marketing", total.multiply(BigDecimal.valueOf(0.25)).setScale(SCALE, RM), "Initial launch campaigns, identity"));
        list.add(new StartupCostItemDTO("Legal & Licensing", total.multiply(BigDecimal.valueOf(0.10)).setScale(SCALE, RM), "Entity registration, permits"));
        list.add(new StartupCostItemDTO("Equipment & Office", total.multiply(BigDecimal.valueOf(0.15)).setScale(SCALE, RM), "Workstations, supplies, fixtures"));
        list.add(new StartupCostItemDTO("Initial Working Capital", total.multiply(BigDecimal.valueOf(0.30)).setScale(SCALE, RM), "Buffer for first 3-6 months"));
        return list;
    }

    public BreakEvenAnalysisDTO calculateBreakEven(BigDecimal price, BigDecimal varCost, BigDecimal monthlyFixedCosts) {
        BigDecimal cm = price.subtract(varCost);
        BigDecimal cmRatio = price.compareTo(BigDecimal.ZERO) > 0 
                ? cm.divide(price, 4, RM).multiply(BigDecimal.valueOf(100)).setScale(SCALE, RM) 
                : BigDecimal.ZERO;

        BigDecimal breakEvenUnitsMonthly = BigDecimal.ZERO;
        BigDecimal breakEvenRevenueMonthly = BigDecimal.ZERO;
        String note;

        if (cm.compareTo(BigDecimal.ZERO) <= 0) {
            note = "Warning: Variable cost exceeds or equals selling price. Unit economics must be improved.";
        } else {
            breakEvenUnitsMonthly = monthlyFixedCosts.divide(cm, 0, RoundingMode.CEILING);
            breakEvenRevenueMonthly = breakEvenUnitsMonthly.multiply(price).setScale(SCALE, RM);
            note = "Healthy contribution margin. Focus on acquiring sufficient volume to reach break-even.";
        }

        BigDecimal annualFixedCosts = monthlyFixedCosts.multiply(BigDecimal.valueOf(12));
        BigDecimal breakEvenUnitsAnnual = breakEvenUnitsMonthly.multiply(BigDecimal.valueOf(12));
        BigDecimal breakEvenRevenueAnnual = breakEvenRevenueMonthly.multiply(BigDecimal.valueOf(12));

        return BreakEvenAnalysisDTO.builder()
                .sellingPrice(price.setScale(SCALE, RM))
                .variableCostPerUnit(varCost.setScale(SCALE, RM))
                .contributionMargin(cm.setScale(SCALE, RM))
                .contributionMarginRatio(cmRatio)
                .monthlyFixedCosts(monthlyFixedCosts.setScale(SCALE, RM))
                .breakEvenUnitsMonthly(breakEvenUnitsMonthly)
                .breakEvenRevenueMonthly(breakEvenRevenueMonthly)
                .breakEvenUnitsAnnual(breakEvenUnitsAnnual)
                .breakEvenRevenueAnnual(breakEvenRevenueAnnual)
                .statusNote(note)
                .build();
    }

    private List<AnnualProjectionDTO> calculateProjections(int years, BigDecimal monthlyUnits, BigDecimal price, BigDecimal varCost, BigDecimal monthlyOpex, int employees) {
        List<AnnualProjectionDTO> list = new ArrayList<>();
        BigDecimal annualUnits = monthlyUnits.multiply(BigDecimal.valueOf(12));
        BigDecimal annualOpex = monthlyOpex.multiply(BigDecimal.valueOf(12));

        // Growth rates: Year 1 = baseline, Year 2 = +40%, Year 3 = +30%, Year 4 = +25%, Year 5 = +20%
        BigDecimal[] growthFactors = {
            BigDecimal.valueOf(1.0),
            BigDecimal.valueOf(1.40),
            BigDecimal.valueOf(1.82),
            BigDecimal.valueOf(2.27),
            BigDecimal.valueOf(2.73)
        };

        for (int i = 0; i < years; i++) {
            int yearNum = i + 1;
            BigDecimal factor = growthFactors[i];

            BigDecimal curUnits = annualUnits.multiply(factor).setScale(0, RM);
            BigDecimal revenue = curUnits.multiply(price).setScale(SCALE, RM);
            BigDecimal cogs = curUnits.multiply(varCost).setScale(SCALE, RM);
            BigDecimal grossProfit = revenue.subtract(cogs).setScale(SCALE, RM);
            BigDecimal grossMarginPct = revenue.compareTo(BigDecimal.ZERO) > 0 
                    ? grossProfit.divide(revenue, 4, RM).multiply(BigDecimal.valueOf(100)).setScale(SCALE, RM) 
                    : BigDecimal.ZERO;

            // Opex scales slightly with growth (+15% per year)
            BigDecimal yearOpexFactor = BigDecimal.valueOf(Math.pow(1.15, i));
            BigDecimal opex = annualOpex.multiply(yearOpexFactor).setScale(SCALE, RM);

            BigDecimal ebitda = grossProfit.subtract(opex).setScale(SCALE, RM);
            BigDecimal depreciation = opex.multiply(BigDecimal.valueOf(0.05)).setScale(SCALE, RM);
            BigDecimal ebit = ebitda.subtract(depreciation).setScale(SCALE, RM);
            BigDecimal taxes = ebit.compareTo(BigDecimal.ZERO) > 0 
                    ? ebit.multiply(BigDecimal.valueOf(0.20)).setScale(SCALE, RM) 
                    : BigDecimal.ZERO;
            BigDecimal netProfit = ebit.subtract(taxes).setScale(SCALE, RM);
            BigDecimal netMarginPct = revenue.compareTo(BigDecimal.ZERO) > 0 
                    ? netProfit.divide(revenue, 4, RM).multiply(BigDecimal.valueOf(100)).setScale(SCALE, RM) 
                    : BigDecimal.ZERO;

            list.add(AnnualProjectionDTO.builder()
                    .year(yearNum)
                    .revenue(revenue)
                    .cogs(cogs)
                    .grossProfit(grossProfit)
                    .grossMarginPercent(grossMarginPct)
                    .operatingExpenses(opex)
                    .ebitda(ebitda)
                    .depreciation(depreciation)
                    .taxes(taxes)
                    .netProfit(netProfit)
                    .netMarginPercent(netMarginPct)
                    .build());
        }
        return list;
    }

    private FinancialMetricsDTO calculateMetrics(BigDecimal initialInv, BigDecimal monthlyOpex, List<AnnualProjectionDTO> projections, BreakEvenAnalysisDTO be) {
        AnnualProjectionDTO y1 = projections.get(0);
        BigDecimal grossMargin = y1.getGrossMarginPercent();
        BigDecimal netMargin = y1.getNetMarginPercent();
        BigDecimal operatingMargin = y1.getRevenue().compareTo(BigDecimal.ZERO) > 0
                ? y1.getEbitda().divide(y1.getRevenue(), 4, RM).multiply(BigDecimal.valueOf(100)).setScale(SCALE, RM)
                : BigDecimal.ZERO;

        BigDecimal monthlyBurn = monthlyOpex.setScale(SCALE, RM);
        BigDecimal cashRunway = monthlyOpex.compareTo(BigDecimal.ZERO) > 0
                ? initialInv.divide(monthlyOpex, 1, RM)
                : BigDecimal.valueOf(99);

        AnnualProjectionDTO y3 = projections.size() >= 3 ? projections.get(2) : y1;
        BigDecimal roiYear3 = initialInv.compareTo(BigDecimal.ZERO) > 0
                ? y3.getNetProfit().divide(initialInv, 4, RM).multiply(BigDecimal.valueOf(100)).setScale(SCALE, RM)
                : BigDecimal.ZERO;

        String breakEvenPeriod = "Month " + (be.getBreakEvenUnitsMonthly().compareTo(BigDecimal.ZERO) > 0 ? "6 - 9" : "N/A");

        return FinancialMetricsDTO.builder()
                .grossProfitMargin(grossMargin)
                .netProfitMargin(netMargin)
                .operatingMargin(operatingMargin)
                .monthlyBurnRate(monthlyBurn)
                .cashRunwayMonths(cashRunway)
                .estimatedRoiYear3(roiYear3)
                .breakEvenPeriod(breakEvenPeriod)
                .build();
    }

    private List<ScenarioResultDTO> calculateScenarios(BigDecimal monthlyUnits, BigDecimal price, BigDecimal varCost, BigDecimal monthlyOpex, int employees) {
        List<ScenarioResultDTO> list = new ArrayList<>();

        // Base Scenario
        List<AnnualProjectionDTO> baseProj = calculateProjections(3, monthlyUnits, price, varCost, monthlyOpex, employees);
        list.add(ScenarioResultDTO.builder()
                .scenarioName("Base Case")
                .annualRevenueYear1(baseProj.get(0).getRevenue())
                .annualRevenueYear2(baseProj.get(1).getRevenue())
                .annualRevenueYear3(baseProj.get(2).getRevenue())
                .annualNetProfitYear1(baseProj.get(0).getNetProfit())
                .annualNetProfitYear2(baseProj.get(1).getNetProfit())
                .annualNetProfitYear3(baseProj.get(2).getNetProfit())
                .breakEvenMonth(BigDecimal.valueOf(8))
                .build());

        // Conservative Scenario (-25% units, +15% opex)
        BigDecimal consUnits = monthlyUnits.multiply(BigDecimal.valueOf(0.75)).setScale(0, RM);
        BigDecimal consOpex = monthlyOpex.multiply(BigDecimal.valueOf(1.15)).setScale(SCALE, RM);
        List<AnnualProjectionDTO> consProj = calculateProjections(3, consUnits, price, varCost, consOpex, employees);
        list.add(ScenarioResultDTO.builder()
                .scenarioName("Conservative")
                .annualRevenueYear1(consProj.get(0).getRevenue())
                .annualRevenueYear2(consProj.get(1).getRevenue())
                .annualRevenueYear3(consProj.get(2).getRevenue())
                .annualNetProfitYear1(consProj.get(0).getNetProfit())
                .annualNetProfitYear2(consProj.get(1).getNetProfit())
                .annualNetProfitYear3(consProj.get(2).getNetProfit())
                .breakEvenMonth(BigDecimal.valueOf(14))
                .build());

        // Optimistic Scenario (+35% units, -5% opex)
        BigDecimal optUnits = monthlyUnits.multiply(BigDecimal.valueOf(1.35)).setScale(0, RM);
        BigDecimal optOpex = monthlyOpex.multiply(BigDecimal.valueOf(0.95)).setScale(SCALE, RM);
        List<AnnualProjectionDTO> optProj = calculateProjections(3, optUnits, price, varCost, optOpex, employees);
        list.add(ScenarioResultDTO.builder()
                .scenarioName("Optimistic")
                .annualRevenueYear1(optProj.get(0).getRevenue())
                .annualRevenueYear2(optProj.get(1).getRevenue())
                .annualRevenueYear3(optProj.get(2).getRevenue())
                .annualNetProfitYear1(optProj.get(0).getNetProfit())
                .annualNetProfitYear2(optProj.get(1).getNetProfit())
                .annualNetProfitYear3(optProj.get(2).getNetProfit())
                .breakEvenMonth(BigDecimal.valueOf(5))
                .build());

        return list;
    }

    private List<Map<String, Object>> generateMonthlyChartData(BigDecimal baseMonthlyUnits, BigDecimal price, BigDecimal varCost, BigDecimal monthlyOpex) {
        List<Map<String, Object>> points = new ArrayList<>();
        String[] months = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10", "M11", "M12"};
        BigDecimal rampRate = BigDecimal.valueOf(0.60); // Starts at 60% in M1, ramps to 130% in M12

        for (int i = 0; i < 12; i++) {
            double ramp = 0.50 + (i * 0.08);
            BigDecimal currentUnits = baseMonthlyUnits.multiply(BigDecimal.valueOf(ramp)).setScale(0, RM);
            BigDecimal revenue = currentUnits.multiply(price).setScale(SCALE, RM);
            BigDecimal cogs = currentUnits.multiply(varCost).setScale(SCALE, RM);
            BigDecimal totalExpenses = monthlyOpex.add(cogs).setScale(SCALE, RM);
            BigDecimal profit = revenue.subtract(totalExpenses).setScale(SCALE, RM);

            Map<String, Object> point = new LinkedHashMap<>();
            point.put("month", months[i]);
            point.put("revenue", revenue.doubleValue());
            point.put("expenses", totalExpenses.doubleValue());
            point.put("profit", profit.doubleValue());
            points.add(point);
        }
        return points;
    }

    private BigDecimal defaultIfNull(BigDecimal val, BigDecimal fallback) {
        return (val != null && val.compareTo(BigDecimal.ZERO) >= 0) ? val : fallback;
    }
}
