package com.bpg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

public class AiDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SectionRegenerateRequest {
        private String sectionKey;
        private String instruction; // e.g. "expand", "simplify", "investor-ready", "bank-loan"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AiChatRequest {
        private String message;
        private String currentSectionKey;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AiChatResponse {
        private String reply;
        private List<String> suggestions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GeneratedPlanDTO {
        private String businessName;
        private String executiveSummary;
        private Map<String, String> sections;
    }
}
