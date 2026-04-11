package com.bindglam.agora.school.service;

import com.bindglam.agora.school.MealMenu;
import com.bindglam.agora.school.PerformanceAssessmentDTO;
import com.bindglam.agora.school.SchoolRequests;
import com.bindglam.agora.school.Timetable;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SchoolService {
    @NonNull CompletableFuture<@NonNull Timetable> getTimetable(@NonNull LocalDate date);

    @NonNull CompletableFuture<@NonNull MealMenu> getMealMenu(@NonNull LocalDate date);

    @NonNull List<PerformanceAssessmentDTO> getPerformanceAssessments(@NonNull LocalDate date);

    SchoolRequests.PerformanceAssessment.Create.@NonNull Response createPerformanceAssessment(SchoolRequests.PerformanceAssessment.@NonNull Create request);

    SchoolRequests.PerformanceAssessment.Edit.@NonNull Response editPerformanceAssessment(long id, SchoolRequests.PerformanceAssessment.@NonNull Edit request);
}
