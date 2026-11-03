package com.bpg.repository;

import com.bpg.entity.BusinessPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessPlanRepository extends JpaRepository<BusinessPlan, Long> {
    Page<BusinessPlan> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
    List<BusinessPlan> findByUserIdOrderByUpdatedAtDesc(Long userId);
    Optional<BusinessPlan> findByIdAndUserId(Long id, Long userId);
    long countByUserId(Long userId);
}
