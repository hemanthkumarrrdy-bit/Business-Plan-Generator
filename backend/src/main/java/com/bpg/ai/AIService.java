package com.bpg.ai;

import com.bpg.dto.AiDTOs.AiChatResponse;
import com.bpg.dto.BusinessPlanDTOs.CreatePlanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final GeminiAIProvider geminiAIProvider;
    private final DemoBusinessPlanGenerator demoGenerator;

    public Map<String, String> generatePlan(CreatePlanRequest request) {
        if (geminiAIProvider.isConfigured()) {
            return geminiAIProvider.generatePlanSections(request);
        }
        return demoGenerator.generatePlanSections(request);
    }

    public String regenerateSection(String businessName, String industry, String sectionKey, String currentContent, String instruction) {
        if (geminiAIProvider.isConfigured()) {
            return geminiAIProvider.regenerateSection(businessName, industry, sectionKey, currentContent, instruction);
        }
        return demoGenerator.regenerateSection(businessName, industry, sectionKey, currentContent, instruction);
    }

    public AiChatResponse chat(String businessName, String industry, String message, String currentSectionKey, String planContext) {
        if (geminiAIProvider.isConfigured()) {
            return geminiAIProvider.chatWithAssistant(businessName, industry, message, currentSectionKey, planContext);
        }
        return demoGenerator.chatWithAssistant(businessName, industry, message, currentSectionKey, planContext);
    }
}
