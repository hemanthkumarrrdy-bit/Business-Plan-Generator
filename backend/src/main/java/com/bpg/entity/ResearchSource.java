package com.bpg.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "research_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long businessPlanId;

    private String title;

    @Column(length = 500)
    private String url;

    private String sourceType;

    @Builder.Default
    private Instant retrievedAt = Instant.now();
}
