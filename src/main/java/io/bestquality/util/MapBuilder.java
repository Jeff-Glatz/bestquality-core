package io.bestquality.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private final Map<K, V> map;

    public MapBuilder() {
        this(new LinkedHashMap<>());
    }

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder<K, V> with(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }

    public static <K, V> MapBuilder<K, V> mapOf(Class<K> key, Class<V> value) {
        return new MapBuilder<>();
    }

    public static <K, V> MapBuilder<K, V> mapOf(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    public static <K, V> MapBuilder<K, V> newMap(Class<K> key, Class<V> value) {
        return mapOf(key, value);
    }

    public static <K, V> MapBuilder<K, V> newMap(Map<K, V> map) {
        return mapOf(map);
    }
}
