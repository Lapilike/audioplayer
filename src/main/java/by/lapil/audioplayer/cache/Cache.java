package by.lapil.audioplayer.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Cache<T> {
    private static final Integer CACHE_SIZE = 10;
    private final Map<String, T> cache;

    public Cache() {
        this.cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true);
    }

    public T get(String key) {
        return cache.get(key);
    }

    public void put(String key, T value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}
