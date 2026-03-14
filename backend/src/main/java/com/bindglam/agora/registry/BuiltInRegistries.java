package com.bindglam.agora.registry;

import com.bindglam.agora.board.Board;
import com.bindglam.agora.utils.Identified;
import org.springframework.data.util.Pair;

import java.util.List;

public final class BuiltInRegistries {
    public static final Registry<Board> BOARDS = mappedIdentified(List.of(
            Board.builder().id("free").build()
    ));

    private BuiltInRegistries() {
    }

    private static <T> Registry<T> mapped(List<Pair<String, T>> map) {
        return new MappedRegistry<>(map);
    }

    private static <T extends Identified> Registry<T> mappedIdentified(List<T> map) {
        return new MappedRegistry<>(map.stream().map(it -> Pair.of(it.id(), it)).toList());
    }
}
