package com.bpg.ai;

import com.bpg.dto.AiDTOs.AiChatResponse;
import com.bpg.dto.BusinessPlanDTOs.CreatePlanRequest;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DemoBusinessPlanGenerator implements AIProvider {

    @Override
    public Map<String, String> generatePlanSections(CreatePlanRequest req) {
        String name = req.getBusinessName() != null ? req.getBusinessName() : "Venture Launch";
        String industry = req.getIndustry() != null ? req.getIndustry() : "Technology & Services";
        String type = req.getBusinessType() != null ? req.getBusinessType() : "Startup";
        String audience = req.getTargetAudience() != null ? req.getTargetAudience() : "Forward-thinking business professionals and modern consumers";
        String pricing = req.getPricingStrategy() != null ? req.getPricingStrategy() : "Tiered Value-Based Pricing";
        String revenue = req.getRevenueModel() != null ? req.getRevenueModel() : "Subscription & Service Contracts";
        String usp = req.getUniqueSellingProposition() != null ? req.getUniqueSellingProposition() : "Unmatched speed, automated efficiency, and modern design";
        String country = req.getCountry() != null ? req.getCountry() : "Global Market";

        Map<String, String> sections = new LinkedHashMap<>();

        // 1. Executive Summary
        sections.put("executive_summary",
                "### Executive Summary\n\n" +
                "**" + name + "** is a next-generation " + type.toLowerCase() + " positioned within the **" + industry + "** industry. " +
                "Headquartered in " + country + ", the venture is built to deliver high-impact value to " + audience + ".\n\n" +
                "By leveraging modern technology, scalable architecture, and an obsessive focus on user experience, " + name +
                " aims to solve pervasive market inefficiencies. Our primary unique selling proposition — *" + usp + "* — creates a defensible wedge that enables rapid customer acquisition and sustainable retention.\n\n" +
                "Over the next 3 to 5 years, the company forecasts rapid compound annual growth, reaching positive EBITDA and solid operational margins through disciplined unit economics.");

        // 2. Business Overview
        sections.put("business_overview",
                "### Business Overview\n\n" +
                name + " operates as a dedicated " + type + " venture. It was formed to capitalize on structural shifts in " + industry + ",\n" +
                "where traditional competitors have been slow to modernize digital workflows and transparent service models.");

        // 3. Company Description
        sections.put("company_description",
                "### Company Description\n\n" +
                "Registered and operating in " + country + ", " + name + " is structured for high agility. " +
                "The corporate governance emphasizes technical excellence, ethical data handling, and customer-centric value creation.");

        // 4. Mission and Vision
        sections.put("mission_and_vision",
                "### Mission & Vision\n\n" +
                "**Mission Statement:** To empower " + audience + " with seamless, transparent, and superior solutions in " + industry + ".\n\n" +
                "**Vision Statement:** To become the market-defining standard in " + industry + " recognized globally for innovation, trust, and measurable customer ROI.");

        // 5. Business Objectives
        sections.put("business_objectives",
                "### Strategic Business Objectives\n\n" +
                "1. **Year 1:** Complete product launch, achieve product-market fit, and onboard the first cohort of verified active accounts.\n" +
                "2. **Year 2:** Scale customer acquisition by 140% through organic word-of-mouth and targeted performance marketing.\n" +
                "3. **Year 3:** Reach cash flow positivity, expand international footprint, and maintain net churn under 3.5%.");

        // 6. Problem Statement
        sections.put("problem_statement",
                "### Problem Statement\n\n" +
                "The target market of " + audience + " faces persistent hurdles:\n" +
                "- **Fragmented Tools & Legacy Friction:** Current workflows require navigating multiple disjointed tools with high latency.\n" +
                "- **Opaque Pricing & Hidden Costs:** Legacy options provide poor visibility into total cost of ownership.\n" +
                "- **Lack of Modern Automation:** Manual repetitive tasks consume up to 40% of productive hours.");

        // 7. Proposed Solution
        sections.put("proposed_solution",
                "### Proposed Solution\n\n" +
                name + " provides an integrated end-to-end platform engineered from first principles:\n" +
                "- Unified single-pane-of-glass interface built on modern cloud architecture.\n" +
                "- Automated intelligence that reduces cycle times by 65%.\n" +
                "- Predictable, transparent pricing backed by measurable guarantees.");

        // 8. Products and Services
        sections.put("products_and_services",
                "### Products & Services Portfolio\n\n" +
                "- **Core Tier:** Essential self-service features tailored for initial onboarding.\n" +
                "- **Professional Suite:** Advanced collaboration, automation, and analytics modules for power users.\n" +
                "- **Enterprise Solutions:** Bespoke integrations, dedicated SLAs, and custom workflow compliance.");

        // 9. Market Research
        sections.put("market_research",
                "### Market Research & Landscape\n\n" +
                "The addressable market within " + industry + " is experiencing steady expansion, driven by digitization and remote operations. " +
                "Independent analysts project an industry CAGR of 12.8% through 2030, with increasing demand for unified platforms.");

        // 10. Industry Analysis
        sections.put("industry_analysis",
                "### Industry Analysis (PESTLE Framework)\n\n" +
                "- **Political/Regulatory:** Increasing emphasis on consumer privacy and data governance.\n" +
                "- **Economic:** Macro focus on operational efficiency and vendor consolidation favor high-ROI software.\n" +
                "- **Technological:** Accelerating AI advancements and cloud-native microservices lower infrastructure overhead.");

        // 11. Target Market
        sections.put("target_market",
                "### Target Market Demographics\n\n" +
                "Our primary customer base includes: " + audience + ".\n" +
                "- **Geographic Focus:** " + country + " initially, followed by English-speaking global markets.\n" +
                "- **Psychographics:** Innovative, ROI-driven, prioritizing ease-of-use and reliability.");

        // 12. Customer Personas
        sections.put("customer_personas",
                "### Ideal Customer Persona\n\n" +
                "**Persona: 'Alex - The Progressive Leader'**\n" +
                "- **Role:** Founder, Director, or Operational Lead\n" +
                "- **Key Goal:** Streamline workflows and scale output without exploding headcount.\n" +
                "- **Pain Point:** Frustrated by legacy software clutter and slow customer support.");

        // 13. Competitor Analysis
        sections.put("competitor_analysis",
                "### Competitor Analysis & Differentiation\n\n" +
                "| Competitor | Strengths | Vulnerabilities | " + name + "'s Edge |\n" +
                "|---|---|---|---|\n" +
                "| Legacy Incumbents | Established brand name | High price, sluggish UX | Modern, intuitive, 3x faster |\n" +
                "| Point Solutions | Niche specialization | Limited scope, integration fatigue | Unified all-in-one suite |\n" +
                "| Low-cost Alternatives | Cheap entry barrier | Poor reliability, zero support | Enterprise-grade stability |");

        // 14. SWOT Analysis
        sections.put("swot_analysis",
                "### SWOT Analysis\n\n" +
                "**Strengths:**\n- Lean cost structure and modern tech stack.\n- Propitiatory workflows designed specifically for " + audience + ".\n\n" +
                "**Weaknesses:**\n- Early brand recognition requiring focused market education.\n\n" +
                "**Opportunities:**\n- Rapid expansion into adjacent regional markets.\n- Ecosystem integrations and strategic API partnerships.\n\n" +
                "**Threats:**\n- Fast-following feature clones by well-funded legacy players.");

        // 15. Business Model
        sections.put("business_model",
                "### Business Model Canvas\n\n" +
                "Operating under a " + revenue + " model, " + name + " aligns incentives directly with client success. " +
                "High gross margins (~75-80%) provide significant free cash flow reinvestment into product enhancement.");

        // 16. Revenue Streams
        sections.put("revenue_streams",
                "### Primary Revenue Streams\n\n" +
                "1. **Monthly & Annual Recurring Licenses:** Predictable recurring subscriptions.\n" +
                "2. **Usage & Volume Add-ons:** Value-aligned expansion revenue as customer workload increases.\n" +
                "3. **Professional Onboarding & Training:** One-time premium deployment assistance.");

        // 17. Pricing Strategy
        sections.put("pricing_strategy",
                "### Pricing Strategy\n\n" +
                "Adopting a **" + pricing + "** framework designed to capture customer surplus while removing adoption hesitation.\n" +
                "- Free trial with low barrier to entry.\n" +
                "- Transparent pricing tiers without surprise charges.");

        // 18. Marketing Strategy
        sections.put("marketing_strategy",
                "### Marketing & Customer Acquisition Strategy\n\n" +
                "- **Inbound Authority:** Search-optimized technical deep dives, industry benchmark reports, and interactive tool calculators.\n" +
                "- **Product-Led Growth:** Seamless user invitations and collaborative sharing mechanics.\n" +
                "- **Strategic Partnerships:** Co-marketing initiatives with complementary non-competing service providers.");

        // 19. Sales Strategy
        sections.put("sales_strategy",
                "### Sales & Pipeline Strategy\n\n" +
                "A hybrid high-velocity sales motion combining automated digital checkout for self-serve tiers with dedicated consultation for mid-market and enterprise inquiries.");

        // 20. Operations Plan
        sections.put("operations_plan",
                "### Operational Workflows\n\n" +
                "Operations are organized into cross-functional pods: Product Engineering, Customer Success, and Revenue Operations. " +
                "Automated CI/CD deployment pipelines ensure continuous iteration with zero downtime.");

        // 21. Technology Requirements
        sections.put("technology_requirements",
                "### Technology Stack & Infrastructure\n\n" +
                "- **Backend:** Java 21, Spring Boot microservices, high-throughput REST APIs.\n" +
                "- **Frontend:** React, TypeScript, Tailwind CSS, responsive SPA architecture.\n" +
                "- **Cloud:** Containerized Docker deployments, automated auto-scaling, PostgreSQL managed clustering.");

        // 22. Management Structure
        sections.put("management_structure",
                "### Management & Governance\n\n" +
                "- **Chief Executive Officer:** Vision, capital allocation, and investor relations.\n" +
                "- **Chief Technology Officer:** Architecture, cybersecurity, and engineering leadership.\n" +
                "- **Head of Growth:** Demand generation, community engagement, and lifecycle retention.");

        // 23. Startup Cost Analysis
        sections.put("startup_cost_analysis",
                "### Startup Capital Requirements\n\n" +
                "Initial capital is deployed strategically across critical value drivers: product engineering (20%), go-to-market launch (25%), regulatory compliance (10%), operational tooling (15%), and working capital reserve (30%).");

        // 24. Financial Projections
        sections.put("financial_projections",
                "### Multi-Year Financial Projections Summary\n\n" +
                "Projections indicate steady top-line acceleration. Year 1 revenue scales on initial customer validation; Year 2 achieves operational economies of scale; Year 3 delivers sustained profitability with positive free cash flow.");

        // 25. Break-Even Analysis
        sections.put("break_even_analysis",
                "### Break-Even Feasibility\n\n" +
                "With disciplined fixed monthly operational overhead and favorable contribution margin per unit, the enterprise achieves operational break-even within the first 6 to 9 months of active customer ramp.");

        // 26. Risk Assessment
        sections.put("risk_assessment",
                "### Risk Assessment Matrix\n\n" +
                "- **Market Adoption Risk:** Lower than expected initial conversion rates.\n" +
                "- **Competitive Response Risk:** Legacy competitors dropping prices.\n" +
                "- **Technical Scalability Risk:** Server load spikes during rapid customer onboarding.");

        // 27. Risk Mitigation
        sections.put("risk_mitigation",
                "### Proactive Risk Mitigation Strategies\n\n" +
                "- Agile sprints with weekly customer interviews to ensure immediate product-market alignment.\n" +
                "- Superior product ergonomics and higher switching costs via proprietary data integrations.\n" +
                "- Elastic cloud auto-scaling with 99.9% uptime service level agreements.");

        // 28. Implementation Roadmap
        sections.put("implementation_roadmap",
                "### Strategic Execution Roadmap\n\n" +
                "- **Phase 1 (Months 1-3):** MVP completion, beta user cohort testing, core feedback integration.\n" +
                "- **Phase 2 (Months 4-6):** Public commercial launch, marketing engine activation, initial revenue milestone.\n" +
                "- **Phase 3 (Months 7-12):** Scale go-to-market, introduce enterprise integrations, achieve operational break-even.");

        // 29. Key Performance Indicators
        sections.put("key_performance_indicators",
                "### Target Key Performance Indicators (KPIs)\n\n" +
                "- **Customer Acquisition Cost (CAC):** Target payback period < 6 months.\n" +
                "- **Monthly Recurring Revenue (MRR) Growth:** > 15% month-over-month in early stages.\n" +
                "- **Net Revenue Retention (NRR):** > 115% through expansion tiers.\n" +
                "- **Customer Satisfaction (CSAT):** > 92%.");

        // 30. Conclusion
        sections.put("conclusion",
                "### Conclusion & Investment Thesis\n\n" +
                "**" + name + "** represents a compelling commercial opportunity in the expanding **" + industry + "** domain. " +
                "With clear differentiation, defensible unit economics, and an execution-focused roadmap, the company is primed to capture meaningful market share and create lasting shareholder value.");

        return sections;
    }

    @Override
    public String regenerateSection(String businessName, String industry, String sectionKey, String currentContent, String instruction) {
        String title = sectionKey.replace("_", " ").toUpperCase();
        return "### " + title + " (Updated for: " + instruction + ")\n\n" +
                "Refined tailored content for **" + businessName + "** in the **" + industry + "** space:\n\n" +
                currentContent.replace("### " + title + "\n\n", "") + "\n\n" +
                "> **Strategic Enhancement (" + instruction + "):** Enhanced to provide deeper qualitative rigor, quantifiable metrics, and defensible positioning aligned with " + instruction + " requirements.";
    }

    @Override
    public AiChatResponse chatWithAssistant(String businessName, String industry, String message, String currentSectionKey, String planContext) {
        String lower = message.toLowerCase();
        String reply;
        List<String> suggestions = new ArrayList<>();

        if (lower.contains("cost") || lower.contains("reduce")) {
            reply = "To optimize startup and operational costs for **" + businessName + "**:\n" +
                    "1. **Leverage Cloud Usage Tiers:** Start with pay-as-you-grow serverless infrastructure before committing to fixed enterprise nodes.\n" +
                    "2. **Performance-Driven Marketing:** Allocate 70% of acquisition budget to high-intent search and organic content rather than broad paid campaigns.\n" +
                    "3. **Automate Operational Routines:** Utilize built-in automation to handle customer onboarding, keeping staff overhead lean.";
            suggestions.add("Suggest three high-margin revenue streams");
            suggestions.add("Explain our break-even point");
        } else if (lower.contains("revenue") || lower.contains("price") || lower.contains("stream")) {
            reply = "Here are 3 high-impact revenue expansion opportunities for **" + businessName + "**:\n" +
                    "1. **Annual Prepaid Incentive:** Offer a 15-20% discount on upfront annual commitments to maximize cash flow.\n" +
                    "2. **Add-on Marketplace / Premium Integrations:** Charge a transaction fee or premium tier for third-party connector tools.\n" +
                    "3. **Priority Support & Compliance SLA:** Target enterprise accounts with dedicated account managers and guaranteed 1-hour response times.";
            suggestions.add("How do we improve customer acquisition?");
            suggestions.add("What are our major business risks?");
        } else if (lower.contains("risk")) {
            reply = "Key vulnerabilities to monitor for **" + businessName + "** in " + industry + ":\n" +
                    "1. **Customer Acquisition Cost Inflation:** Monitor blended CAC across channels to prevent unit economics erosion.\n" +
                    "2. **Feature Parity Clones:** Maintain rapid product release cadences and lock in proprietary workflow hooks.\n" +
                    "3. **Churn in Early Cohorts:** Implement proactive customer success health scores to flag disengaged accounts before renewal dates.";
            suggestions.add("Help me write an investor pitch summary");
            suggestions.add("How do we scale to $1M ARR?");
        } else {
            reply = "Regarding your question about **" + businessName + "** (" + industry + "):\n\n" +
                    "Focusing on section *" + currentSectionKey + "*, we recommend aligning your strategic value proposition with verifiable customer pain points. " +
                    "Ensure your financial assumptions reflect realistic acquisition cycle times and retain a 3-month working capital cushion.";
            suggestions.add("How can I reduce startup costs?");
            suggestions.add("Suggest three revenue streams");
            suggestions.add("Explain my break-even point");
        }

        return AiChatResponse.builder()
                .reply(reply)
                .suggestions(suggestions)
                .build();
    }
}
