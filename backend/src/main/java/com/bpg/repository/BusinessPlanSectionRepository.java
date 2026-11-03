package com.bpg.repository;

import com.bpg.entity.BusinessPlanSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessPlanSectionRepository extends JpaRepository<BusinessPlanSection, Long> {
    List<BusinessPlanSection> findByBusinessPlanIdOrderBySortOrderAsc(Long businessPlanId);
    Optional<BusinessPlanSection> findByBusinessPlanIdAndSectionKey(Long businessPlanId, String sectionKey);
}
