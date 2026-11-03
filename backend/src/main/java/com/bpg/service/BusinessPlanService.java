package com.bpg.service;

import com.bpg.ai.AIService;
import com.bpg.dto.AiDTOs.AiChatResponse;
import com.bpg.dto.BusinessPlanDTOs.*;
import com.bpg.dto.FinancialDTOs.*;
import com.bpg.entity.BusinessPlan;
import com.bpg.entity.BusinessPlanSection;
import com.bpg.financial.FinancialEngine;
import com.bpg.repository.BusinessPlanRepository;
import com.bpg.repository.BusinessPlanSectionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessPlanService {

    private final BusinessPlanRepository planRepository;
    private final BusinessPlanSectionRepository sectionRepository;
    private final AIService aiService;
    private final FinancialEngine financialEngine;
    private final ObjectMapper objectMapper;

    @Transactional
    public FullPlanDTO createAndGeneratePlan(Long userId, CreatePlanRequest req) {
        // 1. Calculate financials
        FinancialInputsDTO finInputs = FinancialInputsDTO.builder()
                .currency(req.getCurrency() != null ? req.getCurrency() : "USD")
                .initialInvestment(req.getInitialInvestment() != null ? BigDecimal.valueOf(req.getInitialInvestment()) : BigDecimal.valueOf(30000))
                .monthlyOperatingExpenses(req.getMonthlyOperatingExpenses() != null ? BigDecimal.valueOf(req.getMonthlyOperatingExpenses()) : BigDecimal.valueOf(5000))
                .expectedMonthlyUnits(req.getExpectedMonthlySales() != null ? BigDecimal.valueOf(req.getExpectedMonthlySales()) : BigDecimal.valueOf(150))
                .averageSellingPrice(req.getAverageSellingPrice() != null ? BigDecimal.valueOf(req.getAverageSellingPrice()) : BigDecimal.valueOf(100))
                .variableCostPerUnit(req.getVariableCostPerUnit() != null ? BigDecimal.valueOf(req.getVariableCostPerUnit()) : BigDecimal.valueOf(25))
                .employeeCount(req.getEmployeeCount() != null ? req.getEmployeeCount() : 3)
                .forecastYears(req.getForecastDuration() != null && req.getForecastDuration().contains("5") ? 5 : 3)
                .build();

        FullFinancialModelDTO finModel = financialEngine.calculateFullModel(finInputs);

        String inputsJson = serialize(req);
        String financialJson = serialize(finModel);

        // 2. Persist plan entity
        BusinessPlan plan = BusinessPlan.builder()
                .userId(userId)
                .businessName(req.getBusinessName())
                .industry(req.getIndustry())
                .businessType(req.getBusinessType())
                .businessStage(req.getBusinessStage())
                .country(req.getCountry())
                .currency(req.getCurrency() != null ? req.getCurrency() : "USD")
                .status("COMPLETED")
                .generationInputs(inputsJson)
                .financialData(financialJson)
                .build();

        plan = planRepository.save(plan);

        // 3. AI Generation of 30 sections
        Map<String, String> rawSections = aiService.generatePlan(req);

        List<BusinessPlanSection> sectionEntities = new ArrayList<>();
        int order = 1;
        for (Map.Entry<String, String> entry : rawSections.entrySet()) {
            String title = formatTitle(entry.getKey());
            BusinessPlanSection section = BusinessPlanSection.builder()
                    .businessPlan(plan)
                    .sectionKey(entry.getKey())
                    .sectionTitle(title)
                    .sectionContent(entry.getValue())
                    .sortOrder(order++)
                    .build();
            sectionEntities.add(section);
        }

        sectionRepository.saveAll(sectionEntities);
        plan.setSections(sectionEntities);

        return mapToFullPlanDTO(plan, finModel);
    }

    public List<PlanSummaryDTO> getUserPlans(Long userId) {
        return planRepository.findByUserIdOrderByUpdatedAtDesc(userId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    public FullPlanDTO getPlanById(Long id, Long userId) {
        BusinessPlan plan = planRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));

        List<BusinessPlanSection> sections = sectionRepository.findByBusinessPlanIdOrderBySortOrderAsc(plan.getId());
        plan.setSections(sections);

        return mapToFullPlanDTO(plan, deserializeFinancials(plan.getFinancialData()));
    }

    @Transactional
    public SectionDTO updateSection(Long planId, String sectionKey, String content, Long userId) {
        BusinessPlan plan = planRepository.findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));

        BusinessPlanSection section = sectionRepository.findByBusinessPlanIdAndSectionKey(plan.getId(), sectionKey)
                .orElseThrow(() -> new IllegalArgumentException("Section not found: " + sectionKey));

        section.setSectionContent(content);
        section = sectionRepository.save(section);

        plan.onUpdate();
        planRepository.save(plan);

        return mapToSectionDTO(section);
    }

    @Transactional
    public SectionDTO regenerateSection(Long planId, String sectionKey, String instruction, Long userId) {
        BusinessPlan plan = planRepository.findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));

        BusinessPlanSection section = sectionRepository.findByBusinessPlanIdAndSectionKey(plan.getId(), sectionKey)
                .orElseThrow(() -> new IllegalArgumentException("Section not found: " + sectionKey));

        String newContent = aiService.regenerateSection(
                plan.getBusinessName(),
                plan.getIndustry(),
                sectionKey,
                section.getSectionContent(),
                instruction
        );

        section.setSectionContent(newContent);
        section = sectionRepository.save(section);

        return mapToSectionDTO(section);
    }

    public AiChatResponse chatWithAssistant(Long planId, String message, String currentSectionKey, Long userId) {
        BusinessPlan plan = planRepository.findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));

        return aiService.chat(
                plan.getBusinessName(),
                plan.getIndustry(),
                message,
                currentSectionKey,
                "Stage: " + plan.getBusinessStage() + ", Type: " + plan.getBusinessType()
        );
    }

    @Transactional
    public void deletePlan(Long id, Long userId) {
        BusinessPlan plan = planRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));
        planRepository.delete(plan);
    }

    @Transactional
    public FullPlanDTO duplicatePlan(Long id, Long userId) {
        BusinessPlan original = planRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Business plan not found or unauthorized"));

        BusinessPlan copy = BusinessPlan.builder()
                .userId(userId)
                .businessName(original.getBusinessName() + " (Copy)")
                .industry(original.getIndustry())
                .businessType(original.getBusinessType())
                .businessStage(original.getBusinessStage())
                .country(original.getCountry())
                .currency(original.getCurrency())
                .status(original.getStatus())
                .generationInputs(original.getGenerationInputs())
                .financialData(original.getFinancialData())
                .build();

        copy = planRepository.save(copy);

        List<BusinessPlanSection> originalSections = sectionRepository.findByBusinessPlanIdOrderBySortOrderAsc(original.getId());
        List<BusinessPlanSection> copiedSections = new ArrayList<>();

        for (BusinessPlanSection sec : originalSections) {
            copiedSections.add(BusinessPlanSection.builder()
                    .businessPlan(copy)
                    .sectionKey(sec.getSectionKey())
                    .sectionTitle(sec.getSectionTitle())
                    .sectionContent(sec.getSectionContent())
                    .sortOrder(sec.getSortOrder())
                    .build());
        }
        sectionRepository.saveAll(copiedSections);
        copy.setSections(copiedSections);

        return mapToFullPlanDTO(copy, deserializeFinancials(copy.getFinancialData()));
    }

    // --- Helper Mappers ---

    private PlanSummaryDTO mapToSummaryDTO(BusinessPlan p) {
        return PlanSummaryDTO.builder()
                .id(p.getId())
                .businessName(p.getBusinessName())
                .industry(p.getIndustry())
                .businessType(p.getBusinessType())
                .businessStage(p.getBusinessStage())
                .currency(p.getCurrency())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .sectionCount(30)
                .build();
    }

    private FullPlanDTO mapToFullPlanDTO(BusinessPlan p, Object finModel) {
        List<SectionDTO> sectionDTOs = p.getSections() != null
                ? p.getSections().stream().map(this::mapToSectionDTO).collect(Collectors.toList())
                : Collections.emptyList();

        Map<String, Object> inputs = deserializeMap(p.getGenerationInputs());
        Map<String, Object> finData = deserializeMap(serialize(finModel));

        return FullPlanDTO.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .businessName(p.getBusinessName())
                .industry(p.getIndustry())
                .businessType(p.getBusinessType())
                .businessStage(p.getBusinessStage())
                .country(p.getCountry())
                .currency(p.getCurrency())
                .status(p.getStatus())
                .generationInputs(inputs)
                .financialData(finData)
                .sections(sectionDTOs)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private SectionDTO mapToSectionDTO(BusinessPlanSection s) {
        return SectionDTO.builder()
                .id(s.getId())
                .sectionKey(s.getSectionKey())
                .sectionTitle(s.getSectionTitle())
                .sectionContent(s.getSectionContent())
                .sortOrder(s.getSortOrder())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    private String formatTitle(String key) {
        return Arrays.stream(key.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }

    private Map<String, Object> deserializeMap(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyMap();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private FullFinancialModelDTO deserializeFinancials(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, FullFinancialModelDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
