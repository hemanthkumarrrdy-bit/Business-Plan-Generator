package com.bpg.repository;

import com.bpg.entity.AIConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AIConversationRepository extends JpaRepository<AIConversation, Long> {
    Optional<AIConversation> findByBusinessPlanId(Long businessPlanId);
}
