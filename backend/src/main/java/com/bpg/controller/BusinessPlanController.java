package com.bpg.controller;

import com.bpg.dto.AiDTOs.*;
import com.bpg.dto.BusinessPlanDTOs.*;
import com.bpg.entity.BusinessPlan;
import com.bpg.entity.BusinessPlanSection;
import com.bpg.export.DocxExportService;
import com.bpg.export.PdfExportService;
import com.bpg.repository.BusinessPlanRepository;
import com.bpg.repository.BusinessPlanSectionRepository;
import com.bpg.security.JwtUtil;
import com.bpg.service.BusinessPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-plans")
@RequiredArgsConstructor
public class BusinessPlanController {

    private final BusinessPlanService planService;
    private final BusinessPlanRepository planRepository;
    private final BusinessPlanSectionRepository sectionRepository;
    private final PdfExportService pdfExportService;
    private final DocxExportService docxExportService;
    private final JwtUtil jwtUtil;

    private Long getUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Unauthorized");
        }
        return jwtUtil.extractUserId(authHeader.substring(7));
    }

    @PostMapping
    public ResponseEntity<FullPlanDTO> createPlan(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody CreatePlanRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.createAndGeneratePlan(userId, req));
    }

    @GetMapping
    public ResponseEntity<List<PlanSummaryDTO>> getMyPlans(@RequestHeader("Authorization") String auth) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.getUserPlans(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullPlanDTO> getPlan(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.getPlanById(id, userId));
    }

    @PutMapping("/{id}/sections/{sectionKey}")
    public ResponseEntity<SectionDTO> updateSection(
            @PathVariable Long id,
            @PathVariable String sectionKey,
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody UpdateSectionRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.updateSection(id, sectionKey, req.getContent(), userId));
    }

    @PostMapping("/{id}/regenerate-section")
    public ResponseEntity<SectionDTO> regenerateSection(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth,
            @RequestBody SectionRegenerateRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.regenerateSection(id, req.getSectionKey(), req.getInstruction(), userId));
    }

    @PostMapping("/{id}/ai-assistant")
    public ResponseEntity<AiChatResponse> chatWithAssistant(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth,
            @RequestBody AiChatRequest req) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.chatWithAssistant(id, req.getMessage(), req.getCurrentSectionKey(), userId));
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<FullPlanDTO> duplicatePlan(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        Long userId = getUserId(auth);
        return ResponseEntity.ok(planService.duplicatePlan(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        Long userId = getUserId(auth);
        planService.deletePlan(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/export/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        BusinessPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + id));
        List<BusinessPlanSection> sections = sectionRepository.findByBusinessPlanIdOrderBySortOrderAsc(id);

        byte[] pdfBytes = pdfExportService.generatePdf(plan, sections);
        String filename = plan.getBusinessName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_Business_Plan.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/{id}/export/docx")
    public ResponseEntity<byte[]> exportDocx(@PathVariable Long id) {
        BusinessPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + id));
        List<BusinessPlanSection> sections = sectionRepository.findByBusinessPlanIdOrderBySortOrderAsc(id);

        byte[] docxBytes = docxExportService.generateDocx(plan, sections);
        String filename = plan.getBusinessName().replaceAll("[^a-zA-Z0-9.-]", "_") + "_Business_Plan.docx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(docxBytes);
    }
}
