package com.bindglam.agora.school;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public record MealMenu(
        @NotNull List<Meal> meals
) {
    @Builder
    public record Meal(
            @NotNull String name
    ) {
    }
}
