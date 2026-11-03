package com.bpg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "business_plan_sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessPlanSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_plan_id", nullable = false)
    @JsonIgnore
    private BusinessPlan businessPlan;

    @Column(nullable = false, length = 100)
    private String sectionKey;

    @Column(nullable = false)
    private String sectionTitle;

    @Column(columnDefinition = "TEXT")
    private String sectionContent;

    @Builder.Default
    private Integer sortOrder = 0;

    @Builder.Default
    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
