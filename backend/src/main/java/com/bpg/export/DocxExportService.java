package com.bpg.export;

import com.bpg.entity.BusinessPlan;
import com.bpg.entity.BusinessPlanSection;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DocxExportService {

    public byte[] generateDocx(BusinessPlan plan, List<BusinessPlanSection> sections) {
        try (XWPFDocument doc = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Document Title
            XWPFParagraph titlePara = doc.createParagraph();
            titlePara.setAlignment(ParagraphAlignment.CENTER);
            titlePara.setSpacingBefore(1000);
            XWPFRun titleRun = titlePara.createRun();
            titleRun.setText(plan.getBusinessName());
            titleRun.setBold(true);
            titleRun.setFontSize(26);
            titleRun.setColor("1E293B");

            // Subtitle
            XWPFParagraph subPara = doc.createParagraph();
            subPara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun subRun = subPara.createRun();
            subRun.setText("Professional Business Plan");
            subRun.setFontSize(14);
            subRun.setColor("64748B");

            // Page break
            XWPFParagraph breakPara = doc.createParagraph();
            breakPara.setPageBreak(true);

            // Sections
            for (BusinessPlanSection sec : sections) {
                XWPFParagraph headingPara = doc.createParagraph();
                headingPara.setSpacingBefore(300);
                headingPara.setSpacingAfter(100);
                XWPFRun headingRun = headingPara.createRun();
                headingRun.setText(sec.getSectionTitle());
                headingRun.setBold(true);
                headingRun.setFontSize(16);
                headingRun.setColor("7C5CFC");

                XWPFParagraph contentPara = doc.createParagraph();
                contentPara.setSpacingAfter(200);
                XWPFRun contentRun = contentPara.createRun();
                String text = sec.getSectionContent() != null
                        ? sec.getSectionContent().replaceAll("#+\\s*", "")
                        : "";
                contentRun.setText(text);
                contentRun.setFontSize(11);
                contentRun.setColor("334155");
            }

            doc.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering DOCX: " + e.getMessage(), e);
        }
    }
}
