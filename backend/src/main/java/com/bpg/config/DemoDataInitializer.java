package com.bpg.config;

import com.bpg.dto.BusinessPlanDTOs.CreatePlanRequest;
import com.bpg.entity.User;
import com.bpg.repository.UserRepository;
import com.bpg.service.BusinessPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BusinessPlanService planService;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing pre-seeded demo users and plans...");

            // 1. Regular Demo User
            User demoUser = User.builder()
                    .fullName("Alex Morgan")
                    .email("user@example.com")
                    .passwordHash(passwordEncoder.encode("password123"))
                    .role("ROLE_USER")
                    .creditsRemaining(25)
                    .build();
            demoUser = userRepository.save(demoUser);

            // 2. Admin User
            User adminUser = User.builder()
                    .fullName("Admin Officer")
                    .email("admin@example.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .creditsRemaining(100)
                    .build();
            userRepository.save(adminUser);

            // 3. Seed Sample Business Plan
            CreatePlanRequest planReq = CreatePlanRequest.builder()
                    .businessName("NexusFlow AI")
                    .industry("SaaS & Enterprise Workflow")
                    .businessType("SaaS")
                    .businessStage("Growth")
                    .country("United States")
                    .city("San Francisco")
                    .businessDescription("Autonomous AI orchestration platform eliminating repetitive administrative workflows for mid-market knowledge teams.")
                    .targetAudience("Operations leads, CTOs, and modern enterprise teams with 50-500 employees")
                    .pricingStrategy("Tiered Value-Based Subscriptions ($99/mo to $899/mo)")
                    .revenueModel("Monthly & Annual Recurring SaaS Subscriptions")
                    .uniqueSellingProposition("Self-learning workflows with zero-code setup and sub-second execution")
                    .initialInvestment(45000.0)
                    .monthlyOperatingExpenses(6500.0)
                    .expectedMonthlySales(220.0)
                    .averageSellingPrice(150.0)
                    .variableCostPerUnit(15.0)
                    .employeeCount(4)
                    .forecastDuration("3 years")
                    .build();

            planService.createAndGeneratePlan(demoUser.getId(), planReq);
            log.info("Demo user (user@example.com / password123) and sample plan initialized successfully.");
        }
    }
}
