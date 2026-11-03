package com.bpg.ai;

import com.bpg.dto.AiDTOs.AiChatResponse;
import com.bpg.dto.BusinessPlanDTOs.CreatePlanRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class GeminiAIProvider implements AIProvider {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;
    private final ObjectMapper objectMapper;
    private final DemoBusinessPlanGenerator fallbackGenerator;

    public GeminiAIProvider(
            WebClient.Builder webClientBuilder,
            @Value("${app.ai.gemini.api-key:}") String apiKey,
            @Value("${app.ai.gemini.model:gemini-1.5-flash}") String model,
            @Value("${app.ai.gemini.base-url:https://generativelanguage.googleapis.com}") String baseUrl,
            @Value("${app.ai.gemini.timeout-seconds:60}") int timeoutSeconds,
            ObjectMapper objectMapper,
            DemoBusinessPlanGenerator fallbackGenerator) {
        this.apiKey = apiKey;
        this.model = model;
        this.objectMapper = objectMapper;
        this.fallbackGenerator = fallbackGenerator;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.equals("your_gemini_api_key");
    }

    @Override
    public Map<String, String> generatePlanSections(CreatePlanRequest request) {
        if (!isConfigured()) {
            log.info("Gemini API key not configured. Using high-fidelity local generator.");
            return fallbackGenerator.generatePlanSections(request);
        }

        try {
            String prompt = buildPrompt(request);
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    ),
                    "generationConfig", Map.of(
                            "temperature", 0.7,
                            "maxOutputTokens", 8192,
                            "responseMimeType", "application/json"
                    )
            );

            String response = webClient.post()
                    .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            JsonNode root = objectMapper.readTree(response);
            String rawJsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            JsonNode sectionsNode = objectMapper.readTree(rawJsonText);

            Map<String, String> result = new LinkedHashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = sectionsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                result.put(entry.getKey(), entry.getValue().asText());
            }

            if (result.isEmpty()) {
                return fallbackGenerator.generatePlanSections(request);
            }
            return result;
        } catch (Exception e) {
            log.warn("Gemini generation failed: {}. Falling back to demo generator.", e.getMessage());
            return fallbackGenerator.generatePlanSections(request);
        }
    }

    @Override
    public String regenerateSection(String businessName, String industry, String sectionKey, String currentContent, String instruction) {
        if (!isConfigured()) {
            return fallbackGenerator.regenerateSection(businessName, industry, sectionKey, currentContent, instruction);
        }
        try {
            String prompt = "Rewrite and enhance the business plan section '" + sectionKey + "' for business '" + businessName +
                    "' in the '" + industry + "' industry. Instruction: " + instruction + ".\nCurrent content:\n" + currentContent;
            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );
            String response = webClient.post()
                    .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            return root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            log.warn("Gemini section regeneration failed: {}. Falling back.", e.getMessage());
            return fallbackGenerator.regenerateSection(businessName, industry, sectionKey, currentContent, instruction);
        }
    }

    @Override
    public AiChatResponse chatWithAssistant(String businessName, String industry, String message, String currentSectionKey, String planContext) {
        if (!isConfigured()) {
            return fallbackGenerator.chatWithAssistant(businessName, industry, message, currentSectionKey, planContext);
        }
        try {
            String prompt = "You are an expert AI startup advisor assisting on the business plan for '" + businessName + "' in " + industry + ".\n" +
                    "Context: " + planContext + "\nCurrent Section: " + currentSectionKey + "\nUser question: " + message +
                    "\nProvide an insightful, actionable response.";
            Map<String, Object> body = Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
            );
            String response = webClient.post()
                    .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            String reply = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            return AiChatResponse.builder()
                    .reply(reply)
                    .suggestions(List.of("How do we improve unit economics?", "Suggest growth tactics"))
                    .build();
        } catch (Exception e) {
            log.warn("Gemini chat failed: {}. Using fallback.", e.getMessage());
            return fallbackGenerator.chatWithAssistant(businessName, industry, message, currentSectionKey, planContext);
        }
    }

    private String buildPrompt(CreatePlanRequest req) {
        return "You are an elite venture strategist and business analyst. Return a JSON object with 30 keys representing full business plan sections: " +
                "executive_summary, business_overview, company_description, mission_and_vision, business_objectives, problem_statement, proposed_solution, " +
                "products_and_services, market_research, industry_analysis, target_market, customer_personas, competitor_analysis, swot_analysis, business_model, " +
                "revenue_streams, pricing_strategy, marketing_strategy, sales_strategy, operations_plan, technology_requirements, management_structure, " +
                "startup_cost_analysis, financial_projections, break_even_analysis, risk_assessment, risk_mitigation, implementation_roadmap, key_performance_indicators, conclusion.\n\n" +
                "Business Name: " + req.getBusinessName() + "\n" +
                "Industry: " + req.getIndustry() + "\n" +
                "Type: " + req.getBusinessType() + "\n" +
                "Audience: " + req.getTargetAudience() + "\n" +
                "Revenue Model: " + req.getRevenueModel() + "\n" +
                "Pricing Strategy: " + req.getPricingStrategy() + "\n" +
                "USP: " + req.getUniqueSellingProposition() + "\n" +
                "Ensure comprehensive, investor-grade markdown within each section value.";
    }
}
