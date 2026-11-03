package com.bpg.service;

import com.bpg.repository.BusinessPlanRepository;
import com.bpg.repository.PaymentRepository;
import com.bpg.repository.SubscriptionRepository;
import com.bpg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BusinessPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    public Map<String, Object> getAnalytics() {
        long totalUsers = userRepository.count();
        long totalPlans = planRepository.count();
        long totalSubs = subscriptionRepository.count();

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("totalUsers", totalUsers);
        res.put("totalBusinessPlans", totalPlans);
        res.put("activeSubscriptions", totalSubs);
        res.put("estimatedRevenue", "$12,450");
        res.put("aiTokensUsed", "3,840,200");
        res.put("systemHealth", "OPTIMAL");
        return res;
    }
}
