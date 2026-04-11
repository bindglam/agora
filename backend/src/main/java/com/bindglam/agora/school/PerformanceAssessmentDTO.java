package com.bindglam.agora.school;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;

@Builder
public record PerformanceAssessmentDTO(
        Long id,
        String subject,
        String title,
        String description,
        LocalDate evaluateAt
) {
    public @NonNull PerformanceAssessment toEntity() {
        return PerformanceAssessment.builder()
                .id(id)
                .subject(subject)
                .title(title)
                .description(description)
                .evaluateAt(evaluateAt)
                .build();
    }
}
