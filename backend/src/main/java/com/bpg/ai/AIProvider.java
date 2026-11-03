package com.bpg.ai;

import com.bpg.dto.AiDTOs.*;
import com.bpg.dto.BusinessPlanDTOs.CreatePlanRequest;
import java.util.Map;

public interface AIProvider {
    Map<String, String> generatePlanSections(CreatePlanRequest request);
    String regenerateSection(String businessName, String industry, String sectionKey, String currentContent, String instruction);
    AiChatResponse chatWithAssistant(String businessName, String industry, String message, String currentSectionKey, String planContext);
}
