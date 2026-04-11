package com.bindglam.agora.school;

import com.bindglam.agora.utils.Requests;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

public interface SchoolRequests {
    interface PerformanceAssessment {
        @Builder
        record Create(@NonNull String subject, @NonNull String title, @NonNull String description, @NonNull LocalDate evaluateAt, @NonNull String uploaderKey) {
            @Builder
            public record Response(@Nullable PerformanceAssessmentDTO performanceAssessment, @Nullable String errorMessage) implements Requests.Response {
            }
        }

        @Builder
        record Edit(@NonNull String subject, @NonNull String title, @NonNull String description, @NonNull LocalDate evaluateAt, @NonNull String uploaderKey) {
            @Builder
            public record Response(@Nullable PerformanceAssessmentDTO performanceAssessment, @Nullable String errorMessage) implements Requests.Response {
            }
        }
    }
}
