package com.bpg.export;

import com.bpg.entity.BusinessPlan;
import com.bpg.entity.BusinessPlanSection;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] generatePdf(BusinessPlan plan, List<BusinessPlanSection> sections) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 45, 45, 60, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, out);

            // Header & Footer event
            writer.setPageEvent(new PdfPageEventHelper() {
                final Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.GRAY);
                @Override
                public void onEndPage(PdfWriter w, Document d) {
                    if (w.getPageNumber() > 1) {
                        PdfPTable table = new PdfPTable(2);
                        try {
                            table.setWidths(new int[]{70, 30});
                            table.setTotalWidth(505);
                            PdfPCell c1 = new PdfPCell(new Phrase(plan.getBusinessName() + " — Confidential Business Plan", footerFont));
                            c1.setBorder(Rectangle.TOP);
                            c1.setBorderColor(new Color(220, 225, 230));
                            table.addCell(c1);

                            PdfPCell c2 = new PdfPCell(new Phrase("Page " + w.getPageNumber(), footerFont));
                            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            c2.setBorder(Rectangle.TOP);
                            c2.setBorderColor(new Color(220, 225, 230));
                            table.addCell(c2);

                            table.writeSelectedRows(0, -1, 45, 35, w.getDirectContent());
                        } catch (Exception ignored) {}
                    }
                }
            });

            doc.open();

            // COVER PAGE
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, new Color(15, 23, 42));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 14, new Color(100, 116, 139));
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(51, 65, 85));

            Paragraph spacer = new Paragraph(" ");
            spacer.setSpacingBefore(120);
            doc.add(spacer);

            Paragraph title = new Paragraph(plan.getBusinessName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            Paragraph planType = new Paragraph("Comprehensive Investor-Ready Business Plan", subTitleFont);
            planType.setAlignment(Element.ALIGN_CENTER);
            planType.setSpacingBefore(10);
            doc.add(planType);

            Paragraph divider = new Paragraph("— — — — —", subTitleFont);
            divider.setAlignment(Element.ALIGN_CENTER);
            divider.setSpacingBefore(15);
            doc.add(divider);

            Paragraph details = new Paragraph();
            details.setAlignment(Element.ALIGN_CENTER);
            details.setSpacingBefore(40);
            details.add(new Phrase("Industry: " + (plan.getIndustry() != null ? plan.getIndustry() : "Technology") + "\n", metaFont));
            details.add(new Phrase("Market Stage: " + (plan.getBusinessStage() != null ? plan.getBusinessStage() : "Growth") + "\n", metaFont));
            details.add(new Phrase("Geography: " + (plan.getCountry() != null ? plan.getCountry() : "Global") + "\n", metaFont));
            doc.add(details);

            doc.newPage();

            // TABLE OF CONTENTS & SECTIONS
            Font secHeadingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(124, 92, 252));
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(30, 41, 59));
            bodyFont.setLeading(15, 0);

            for (BusinessPlanSection sec : sections) {
                Paragraph secHeading = new Paragraph(sec.getSectionTitle(), secHeadingFont);
                secHeading.setSpacingBefore(15);
                secHeading.setSpacingAfter(8);
                doc.add(secHeading);

                String cleanContent = sec.getSectionContent() != null
                        ? sec.getSectionContent().replaceAll("#+\\s*", "")
                        : "Section content pending.";
                Paragraph secBody = new Paragraph(cleanContent, bodyFont);
                secBody.setSpacingAfter(15);
                doc.add(secBody);
            }

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error rendering PDF: " + e.getMessage(), e);
        }
    }
}
