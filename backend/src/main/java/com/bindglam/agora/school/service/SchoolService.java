package com.bindglam.agora.school.service;

import com.bindglam.agora.school.MealMenu;
import com.bindglam.agora.school.Timetable;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface SchoolService {
    @NonNull CompletableFuture<@NonNull Timetable> getTimetable(@NonNull LocalDate date);

    @NonNull CompletableFuture<@NonNull MealMenu> getMealMenu(@NonNull LocalDate date);
}
