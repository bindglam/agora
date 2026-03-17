package com.bindglam.agora.registry;

import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class MappedRegistry<T> implements Registry<T> {
    private final Map<String, T> map;

    MappedRegistry(Map<String, T> map) {
        this.map = map;
    }

    MappedRegistry(List<Pair<String, T>> map) {
        this(map.stream().collect(Pair.toMap()));
    }

    @Override
    public Optional<T> get(String key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Collection<Map.Entry<String, T>> entries() {
        return map.entrySet();
    }
}
