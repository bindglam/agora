package com.bindglam.agora.school;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PerformanceAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Builder.Default
    private Long id = null;

    @Column(length = 10)
    private String subject;

    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate evaluateAt;

    public @NonNull PerformanceAssessmentDTO toDTO() {
        return PerformanceAssessmentDTO.builder()
                .id(id)
                .subject(subject)
                .title(title)
                .description(description)
                .evaluateAt(evaluateAt)
                .build();
    }
}
