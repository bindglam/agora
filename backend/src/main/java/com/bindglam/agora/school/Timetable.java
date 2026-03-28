package com.bindglam.agora.school;

import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record Timetable(
        @NonNull List<Time> times
) {
    @Builder
    public record Time(
            int position,
            @NonNull String name
    ) implements Comparable<Time> {
        @Override
        public int compareTo(Time o) {
            return Integer.compare(this.position, o.position);
        }
    }
}
